package com.nipplelion.android.groceryapp.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.nipplelion.android.groceryapp.BuildConfig
import com.nipplelion.android.groceryapp.R
import com.nipplelion.android.groceryapp.models.EdamamApiService
import com.nipplelion.android.groceryapp.models.FoodData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

typealias BarcodeListener = (barcode: String) -> Unit

const val BASE_URL = "https://api.edamam.com"
class BarcodeFragment: Fragment(R.layout.fragment_barcode) {

    private var processingBarcode = AtomicBoolean(false) // allows one barcode at a time

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var backButton: ImageButton
    private lateinit var viewFinder: PreviewView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_barcode, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewFinder = view.findViewById(R.id.viewFinder)
        backButton = view.findViewById(R.id.backButton)

        backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        cameraExecutor = Executors.newSingleThreadExecutor()
    }


    private fun getFoodData(barcodeNum: String) {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(EdamamApiService::class.java)

        val retrofitData = retrofitBuilder.getFood(BuildConfig.FOOD_APP_ID,  BuildConfig.FOOD_API_KEY, barcodeNum)

        retrofitData.enqueue(object : Callback<FoodData?> {
            override fun onResponse(
                call: Call<FoodData?>,
                response: Response<FoodData?>
            ) {
                val responseBody = response.body()

                if (responseBody != null) {
                    var foodLabel = responseBody.hints[0].food.label
                    var foodImage = responseBody.hints[0].food.image
                    var foodId = responseBody.hints[0].food.foodId
                    var upc = (responseBody.text)
                    var foodUPC = upc.substring(4, upc.length)

                    parentFragmentManager.commit {
                        var formFragment = FormFragment()
                        var bundle = Bundle()
                        bundle.putString("foodLabel", foodLabel)
                        bundle.putString("foodImage", foodImage)
                        bundle.putString("foodUPC", foodUPC)
                        bundle.putString("foodId", foodId)
                        formFragment.arguments = bundle

                        replace(R.id.fragmentContainerView, formFragment)
                        setReorderingAllowed(true)
                    }
                }
            }

            override fun onFailure(call: Call<FoodData?>, t: Throwable) {
                Log.e("TAG", t.toString())
            }
        })


    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewFinder.surfaceProvider)
                }

            val imageAnalyzer = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, BarcodeAnalyzer { barcodeNum ->
                        //if (processingBarcode.compareAndSet(false, true)) {
                        getFoodData(barcodeNum)
                        //}
                    })
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinging
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalyzer)
            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val TAG = "GroceryApp"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    class BarcodeAnalyzer(private val barcodeListener: BarcodeListener): ImageAnalysis.Analyzer {
        private val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(
        Barcode.FORMAT_UPC_A)
        .build()

        private val scanner = BarcodeScanning.getClient(options)

        @SuppressLint("UnsafeOptInUsageError")
        override fun analyze(imageProxy: ImageProxy) {
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

                scanner.process(image)
                    .addOnSuccessListener { barcodes ->
                        for (barcode in barcodes) {
                            barcodeListener(barcode.rawValue ?: "")
                        }
                    }
                    .addOnFailureListener {
                        Log.e("status", it.toString())
                    }
                    .addOnCompleteListener {
                        imageProxy.close()
                    }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                Toast.makeText(requireContext(),
                    "Permissions already granted by the user.",
                    Toast.LENGTH_SHORT).show()
                startCamera()
            } else {
                Toast.makeText(requireContext(),
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        processingBarcode.set(true)
    }

    override fun onResume() {
        super.onResume()
        processingBarcode.set(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}