package io.legado.app.help.glide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Base64
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import io.legado.app.constant.AppPattern.dataUriRegex
import io.legado.app.model.analyzeRule.AnalyzeUrl
import io.legado.app.utils.isAbsUrl
import io.legado.app.utils.isContentScheme
import java.io.File

@Suppress("unused")
object ImageLoader {

    /**
     * 自动判断path类型
     */
    fun load(context: Context, path: String?): RequestBuilder<Drawable> {
        val dataUriFindResult = dataUriRegex.find(path ?: "")
        return when {
            path.isNullOrEmpty() -> Glide.with(context).load(path)
            dataUriFindResult != null -> kotlin.runCatching {
                val dataUriBase64 = dataUriFindResult.groupValues[1]
                val byteArray = Base64.decode(dataUriBase64, Base64.DEFAULT)
                Glide.with(context).load(byteArray)
            }.getOrDefault(
                GlideApp.with(context).load(path)
            )
            path.isAbsUrl() -> {
                val url = kotlin.runCatching {
                    AnalyzeUrl(path).getGlideUrl()
                }.getOrDefault(path)
                GlideApp.with(context).load(url)
            }
            path.isContentScheme() -> Glide.with(context).load(Uri.parse(path))
            else -> kotlin.runCatching {
                Glide.with(context).load(File(path))
            }.getOrElse {
                Glide.with(context).load(path)
            }
        }
    }

    fun loadBitmap(context: Context, path: String?): RequestBuilder<Bitmap> {
        return when {
            path.isNullOrEmpty() -> Glide.with(context).asBitmap().load(path)
            path.isAbsUrl() -> Glide.with(context).asBitmap().load(AnalyzeUrl(path).getGlideUrl())
            path.isContentScheme() -> Glide.with(context).asBitmap().load(Uri.parse(path))
            else -> kotlin.runCatching {
                Glide.with(context).asBitmap().load(File(path))
            }.getOrElse {
                Glide.with(context).asBitmap().load(path)
            }
        }
    }

    fun loadFile(context: Context, path: String?): RequestBuilder<File> {
        return when {
            path.isNullOrEmpty() -> Glide.with(context).asFile().load(path)
            path.isAbsUrl() -> Glide.with(context).asFile().load(AnalyzeUrl(path).getGlideUrl())
            path.isContentScheme() -> Glide.with(context).asFile().load(Uri.parse(path))
            else -> kotlin.runCatching {
                Glide.with(context).asFile().load(File(path))
            }.getOrElse {
                Glide.with(context).asFile().load(path)
            }
        }
    }

    fun load(context: Context, @DrawableRes resId: Int?): RequestBuilder<Drawable> {
        return Glide.with(context).load(resId)
    }

    fun load(context: Context, file: File?): RequestBuilder<Drawable> {
        return Glide.with(context).load(file)
    }

    fun load(context: Context, uri: Uri?): RequestBuilder<Drawable> {
        return Glide.with(context).load(uri)
    }

    fun load(context: Context, drawable: Drawable?): RequestBuilder<Drawable> {
        return Glide.with(context).load(drawable)
    }

    fun load(context: Context, bitmap: Bitmap?): RequestBuilder<Drawable> {
        return Glide.with(context).load(bitmap)
    }

    fun load(context: Context, bytes: ByteArray?): RequestBuilder<Drawable> {
        return Glide.with(context).load(bytes)
    }

}
