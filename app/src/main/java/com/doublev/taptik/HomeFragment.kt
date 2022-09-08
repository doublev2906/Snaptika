package com.doublev.snaptik

import android.app.DownloadManager
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.doublev.taptik.database.DownloadData
import com.doublev.taptik.database.DownloadDatabase
import com.doublev.taptik.BASE_URL
import com.doublev.taptik.TikTokData
import com.doublev.taptik.TiktokService
import com.doublev.taptik.databinding.FragmentHomeBinding
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imgPaste.setOnClickListener {
            val clipBoard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = clipBoard.primaryClip
            binding.edtUrl.setText(clipData?.getItemAt(0)?.text.toString())
        }
        binding.btnPreview.setOnClickListener {
            getLink()
        }
    }

    override fun onPause() {
        super.onPause()
        binding.videoView.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()

    }



    private fun getLink() {
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        val tiktokService = retrofit.create(TiktokService::class.java)
        val params = HashMap<String, String>()
//        "https://www.tiktok.com/@vuongbinhan/video/7063141700931177754"
        params["url"] = binding.edtUrl.text.toString()
        try {
            tiktokService.getData(params)
                .enqueue(object : Callback<TikTokData> {
                    override fun onResponse(
                        call: Call<TikTokData>,
                        response: Response<TikTokData>
                    ) {
                        val tikTokData = response.body()
                        val data = tikTokData?.data
                        binding.videoView.visibility = View.VISIBLE
                        binding.btnDownloadMp4.visibility = View.VISIBLE
                        binding.btnDownloadMp3.visibility = View.VISIBLE
                        val title = data?.title
                        val thumbnailUrl = data?.origin_cover
                        val id = data?.id
                        val authorName = data?.author?.nickname
                        val downloadData = DownloadData(title = title, thumbnailUrl = thumbnailUrl, author = authorName)

                        preview(data?.play.toString())
                        binding.btnDownloadMp4.setOnClickListener {
                            startDownloadMp4(data?.play.toString())
                            DownloadDatabase.getDatabase(requireContext()).downloadDao().insert(downloadData)
                        }
                        binding.btnDownloadMp3.setOnClickListener {
                            startDownloadMp3(data?.music.toString())
                        }

                        Log.e("AAA", "Success")
                    }

                    override fun onFailure(call: Call<TikTokData>, t: Throwable) {
                        Log.e("AAA", "fail")
                    }

                })
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun startDownloadMp4(url: String) {
        Toast.makeText(context,"Start download video", Toast.LENGTH_SHORT).show()
        val request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI * DownloadManager.Request.NETWORK_MOBILE)
        request.setMimeType("video/mp4")
        request.setTitle("Download")
        request.setDescription("DownLoad file....")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            System.currentTimeMillis().toString()
        )
        val downloadManager = context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }

    private fun startDownloadMp3(url: String) {
        Toast.makeText(context,"Start download music", Toast.LENGTH_SHORT).show()
        val request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI * DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle("Download")
        request.setDescription("DownLoad file....")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            System.currentTimeMillis().toString()
        )
        val downloadManager =context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }

    private fun preview(url: String) {
        binding.videoView.setVideoURI(Uri.parse(url))
        binding.videoView.requestFocus()
        binding.videoView.start()
    }

}