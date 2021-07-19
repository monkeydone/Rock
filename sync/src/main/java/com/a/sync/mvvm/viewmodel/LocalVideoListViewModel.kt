package com.a.sync.mvvm.viewmodel

import android.app.Application
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.a.base.BaseViewModel
import com.a.sync.client.DoKitWsClient.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


class LocalVideoListViewModel(application: Application) :
    BaseViewModel<List<LocalVideoListViewModel.LocalVideoListDataModel>>(application) {

    var dataLive = MutableLiveData<Boolean>()
    var loading = false
    var id = 0L

    var filePath = "/sdcard/QQBrowser/视频/"

    init {
        canReload = true
        canLoadMore = false
    }

    class LocalVideoListDataModel(val file: File, val fileName: String)

    open class VideoInfo {
        var id = 0
        var data: String? = null
        var size: Long = 0
        var displayName: String? = null
        var title: String? = null
        var dateAdded: Long = 0
        var dateModified: Long = 0
        var mimeType: String? = null
        var duration: Long = 0
        var artist: String? = null
        var album: String? = null
        var resolution: String? = null
        var description: String? = null
        var isPrivate = 0
        var tags: String? = null
        var category: String? = null
        var latitude = 0.0
        var longitude = 0.0
        var dateTaken = 0
        var miniThumbMagic = 0
        var bucketId: String? = null
        var bucketDisplayName: String? = null
        var bookmark = 0
        var thumbnailData: String? = null
        var kind = 0
        var width: Long = 0
        var height: Long = 0
        override fun toString(): String {
            return "VideoInfo{" +
                    "id=" + id +
                    ", data='" + data + '\'' +
                    ", size=" + size +
                    ", displayName='" + displayName + '\'' +
                    ", title='" + title + '\'' +
                    ", dateAdded=" + dateAdded +
                    ", dateModified=" + dateModified +
                    ", mimeType='" + mimeType + '\'' +
                    ", duration=" + duration +
                    ", artist='" + artist + '\'' +
                    ", album='" + album + '\'' +
                    ", resolution='" + resolution + '\'' +
                    ", description='" + description + '\'' +
                    ", isPrivate=" + isPrivate +
                    ", tags='" + tags + '\'' +
                    ", category='" + category + '\'' +
                    ", latitude=" + latitude +
                    ", longitude=" + longitude +
                    ", dateTaken=" + dateTaken +
                    ", miniThumbMagic=" + miniThumbMagic +
                    ", bucketId='" + bucketId + '\'' +
                    ", bucketDisplayName='" + bucketDisplayName + '\'' +
                    ", bookmark=" + bookmark +
                    ", thumbnailData='" + thumbnailData + '\'' +
                    ", kind=" + kind +
                    ", width=" + width +
                    ", height=" + height +
                    '}'
        }
    }

    fun loadVideoList(context: Context) {
        initVideoData(context)
        val list = ArrayList<LocalVideoListViewModel.LocalVideoListDataModel>()
        list.clear()
//        mVideoInfos.forEach {
//            list.add(LocalVideoListDataModel(it))
//        }
        itemList.value = list
    }

    fun getFileList(filePath: String): List<File> {
        val list = ArrayList<File>()
        val f = File(filePath)

        if (f.exists() && f.isDirectory) {
            list.addAll(f.listFiles())
        }
        return list
    }

    override fun loadData(): LiveData<List<LocalVideoListViewModel.LocalVideoListDataModel>> {
        val list = ArrayList<LocalVideoListViewModel.LocalVideoListDataModel>()
        viewModelScope.launch {
            list.clear()
            withContext(Dispatchers.IO) {
                val fileList =
                    getFileList(filePath).filter { it.name.endsWith("mp4") or it.name.endsWith("m3u8") }
                list.addAll(fileList.map { LocalVideoListDataModel(it, it.name) })
            }
            itemList.value = list
        }
        return itemList
    }

    private var mVideoInfos: ArrayList<VideoInfo> = ArrayList()

    private val sLocalVideoColumns = arrayOf(
        MediaStore.Video.Media._ID,  // 视频id
        MediaStore.Video.Media.DATA,  // 视频路径
        MediaStore.Video.Media.SIZE,  // 视频字节大小
        MediaStore.Video.Media.DISPLAY_NAME,  // 视频名称 xxx.mp4
        MediaStore.Video.Media.TITLE,  // 视频标题
        MediaStore.Video.Media.DATE_ADDED,  // 视频添加到MediaProvider的时间
        MediaStore.Video.Media.DATE_MODIFIED,  // 上次修改时间，该列用于内部MediaScanner扫描，外部不要修改
        MediaStore.Video.Media.MIME_TYPE,  // 视频类型 video/mp4
        MediaStore.Video.Media.DURATION,  // 视频时长
        MediaStore.Video.Media.ARTIST,  // 艺人名称
        MediaStore.Video.Media.ALBUM,  // 艺人专辑名称
        MediaStore.Video.Media.RESOLUTION,  // 视频分辨率 X x Y格式
        MediaStore.Video.Media.DESCRIPTION,  // 视频描述
        MediaStore.Video.Media.IS_PRIVATE,
        MediaStore.Video.Media.TAGS,
        MediaStore.Video.Media.CATEGORY,  // YouTube类别
        MediaStore.Video.Media.LANGUAGE,  // 视频使用语言
        MediaStore.Video.Media.LATITUDE,  // 拍下该视频时的纬度
        MediaStore.Video.Media.LONGITUDE,  // 拍下该视频时的经度
        MediaStore.Video.Media.DATE_TAKEN,
        MediaStore.Video.Media.MINI_THUMB_MAGIC,
        MediaStore.Video.Media.BUCKET_ID,
        MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Video.Media.BOOKMARK // 上次视频播放的位置
    )
    private val sLocalVideoThumbnailColumns = arrayOf(
        MediaStore.Video.Thumbnails.DATA,  // 视频缩略图路径
        MediaStore.Video.Thumbnails.VIDEO_ID,  // 视频id
        MediaStore.Video.Thumbnails.KIND,
        MediaStore.Video.Thumbnails.WIDTH,  // 视频缩略图宽度
        MediaStore.Video.Thumbnails.HEIGHT // 视频缩略图高度
    )

    private fun initVideoData(context: Context) {
        val cursor: Cursor? = context.getContentResolver().query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI, sLocalVideoColumns,
            null, null, null
        )
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val videoInfo = VideoInfo()
                val id: Int = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media._ID))
                val data: String =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
                val size: Long = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE))
                val displayName: String =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME))
                val title: String =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE))
                val dateAdded: Long =
                    cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED))
                val dateModified: Long =
                    cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED))
                val mimeType: String =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE))
                val duration: Long =
                    cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION))
                val artist: String =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.ARTIST))
                val album: String =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.ALBUM))
                val resolution: String =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.RESOLUTION))
                val description: String =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DESCRIPTION))
                val isPrivate: Int =
                    cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.IS_PRIVATE))
                val tags: String =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TAGS))
                val category: String =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.CATEGORY))
                val latitude: Double =
                    cursor.getDouble(cursor.getColumnIndex(MediaStore.Video.Media.LATITUDE))
                val longitude: Double =
                    cursor.getDouble(cursor.getColumnIndex(MediaStore.Video.Media.LONGITUDE))
                val dateTaken: Int =
                    cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.DATE_TAKEN))
                val miniThumbMagic: Int =
                    cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.MINI_THUMB_MAGIC))
                val bucketId: String =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID))
                val bucketDisplayName: String =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))
                val bookmark: Int =
                    cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.BOOKMARK))
                val thumbnailCursor: Cursor? = context.getContentResolver().query(
                    MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, sLocalVideoThumbnailColumns,
                    MediaStore.Video.Thumbnails.VIDEO_ID + "=" + id, null, null
                )
                if (thumbnailCursor != null && thumbnailCursor.moveToFirst()) {
                    do {
                        val thumbnailData: String =
                            thumbnailCursor.getString(thumbnailCursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA))
                        val kind: Int =
                            thumbnailCursor.getInt(thumbnailCursor.getColumnIndex(MediaStore.Video.Thumbnails.KIND))
                        val width: Long =
                            thumbnailCursor.getLong(thumbnailCursor.getColumnIndex(MediaStore.Video.Thumbnails.WIDTH))
                        val height: Long =
                            thumbnailCursor.getLong(thumbnailCursor.getColumnIndex(MediaStore.Video.Thumbnails.HEIGHT))
                        videoInfo.thumbnailData = thumbnailData
                        videoInfo.kind = kind
                        videoInfo.width = width
                        videoInfo.height = height
                    } while (thumbnailCursor.moveToNext())
                    thumbnailCursor.close()
                }
                videoInfo.id = id
                videoInfo.data = data
                videoInfo.size = size
                videoInfo.displayName = displayName
                videoInfo.title = title
                videoInfo.dateAdded = dateAdded
                videoInfo.dateModified = dateModified
                videoInfo.mimeType = mimeType
                videoInfo.duration = duration
                videoInfo.artist = artist
                videoInfo.album = album
                videoInfo.resolution = resolution
                videoInfo.description = description
                videoInfo.isPrivate = isPrivate
                videoInfo.tags = tags
                videoInfo.category = category
                videoInfo.latitude = latitude
                videoInfo.longitude = longitude
                videoInfo.dateTaken = dateTaken
                videoInfo.miniThumbMagic = miniThumbMagic
                videoInfo.bucketId = bucketId
                videoInfo.bucketDisplayName = bucketDisplayName
                videoInfo.bookmark = bookmark
                Log.v(TAG, "videoInfo = $videoInfo")
                mVideoInfos?.add(videoInfo)
            } while (cursor.moveToNext())
            cursor.close()
        }
    }

    companion object {
        val fileList = ArrayList<LocalVideoListDataModel>()
    }

}