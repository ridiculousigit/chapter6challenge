@file:Suppress("RemoveRedundantQualifierName", "RemoveRedundantQualifierName", "unused", "unused",
    "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused",
    "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused",
    "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused",
    "unused", "unused", "unused", "unused", "unused")

package binar.academy.chapter6challenge.worker

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf

@Suppress("RemoveRedundantQualifierName", "RemoveRedundantQualifierName", "unused", "unused",
    "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused",
    "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused",
    "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused",
    "unused", "unused", "unused", "unused", "unused")
class BlurWorker(context: Context, params : WorkerParameters) : Worker(context,params) {
    override fun doWork(): Result {
        val appContext = applicationContext
        val resourceUri = inputData.getString(KEY_IMAGE_URI)

        makeStatusNotification("Started Blurring image", appContext)

        return try {
            if(TextUtils.isEmpty(resourceUri)){
                Log.e("Uri Status", "Invalid input uri")
                throw IllegalArgumentException("Invalid input uri")
            }

            val resolver = appContext.contentResolver
            val picture = BitmapFactory.decodeStream(
                resolver.openInputStream(Uri.parse(resourceUri)))

            // Get result blur image
            val output = blurBitmap(picture, appContext)

            // Write bitmap to a temp file
            val outputUri = writeBitmapToFile(appContext, output)

            // Create a temporary URI output, so that it can be accessed for the next process
            val outputData = workDataOf(KEY_IMAGE_URI to outputUri.toString())
            ListenableWorker.Result.success(outputData)
        } catch (throwable: Throwable){
            Log.e("Blur Status", "Error applying blur")
            throwable.printStackTrace()
            ListenableWorker.Result.failure()
        }
    }
}