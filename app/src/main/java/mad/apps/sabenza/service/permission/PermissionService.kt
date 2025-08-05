package mad.apps.sabenza.service.permission

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import org.reactivestreams.Subscriber

class PermissionService {

    private val PERMISSIONS_REQUEST_USE_CAMERA = 101
    private val PERMISSIONS_REQUEST_USE_LOCATION = 102

    private var locationPermissionResult: PermissionResult? = null
    private var cameraPermissionResult: PermissionResult? = null
    private var activity: Activity? = null

    fun init(currentActivity: Activity) {
        activity = currentActivity
    }

    fun requestCameraPermission(): Observable<Boolean> {
        return if (activity == null) {
            Observable.just(false)
        } else Observable.create { subscriber ->
            ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.CAMERA), PERMISSIONS_REQUEST_USE_CAMERA)
            cameraPermissionResult = PermissionReceiver(subscriber)
        }
    }

    fun allowedToUseCamera(): Boolean {
        return if (activity == null) {
            false
        } else ContextCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    fun requestLocationPermission(): Observable<Boolean> {
        return if (activity == null) {
            Observable.just(false)
        } else Observable.create { subscriber ->
            ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_REQUEST_USE_LOCATION)
            locationPermissionResult = PermissionReceiver(subscriber)
        }
    }

    fun allowedToUseLocation(): Boolean {
        return if (activity == null) {
            false
        } else ContextCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_USE_CAMERA -> {
                cameraPermissionResult(grantResults)
            }

            PERMISSIONS_REQUEST_USE_LOCATION -> {
                locationPermisionResult(grantResults)
            }
        }
    }

    private fun locationPermisionResult(grantResults: IntArray) {
        if (locationPermissionResult == null) {
            return
        }
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission Granted
            locationPermissionResult!!.permissionGranted()

        } else {
            // Permission Denied
            locationPermissionResult!!.permissionDenied()
        }
        cameraPermissionResult = null
    }

    private fun cameraPermissionResult(grantResults: IntArray) {
        if (cameraPermissionResult == null) {
            return
        }
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission Granted
            cameraPermissionResult!!.permissionGranted()

        } else {
            // Permission Denied
            cameraPermissionResult!!.permissionDenied()
        }
        cameraPermissionResult = null
    }

    interface PermissionResult {

        fun permissionGranted()

        fun permissionDenied()

    }

    private class PermissionReceiver(private val subscriber: ObservableEmitter<in Boolean>) : PermissionResult {

        override fun permissionGranted() {
            subscriber.onNext(true)
            subscriber.onComplete()
        }

        override fun permissionDenied() {
            subscriber.onNext(false)
            subscriber.onComplete()
        }
    }


}
