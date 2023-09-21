package br.com.efabreu.portifolioefabreu.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import br.com.efabreu.portifolioefabreu.R
import br.com.efabreu.portifolioefabreu.data.OwnerApi
import br.com.efabreu.portifolioefabreu.data.RetrofitService
import br.com.efabreu.portifolioefabreu.model.Owner
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.URL


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class GithubFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    lateinit var header :RelativeLayout
    lateinit var tvNome :TextView
    lateinit var tvLogin :TextView
    lateinit var tvBio :TextView
    lateinit var cImagePhoto :CircleImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupScreen(view)
        getUserInfo()

    }

    class LoadImageTask(private val circleImageView: CircleImageView) : AsyncTask<String, Void, Bitmap>()
    {
        override fun doInBackground(vararg urls: String): Bitmap?
        {
            try {
                val stream = URL(urls[0]).openStream()

                return BitmapFactory.decodeStream(stream)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return null
        }

        override fun onPostExecute(bitmap: Bitmap?)
        {
            circleImageView.setImageBitmap(bitmap)
        }
    }

    private fun setupScreen(view :View){
        view.apply {
            header = findViewById(R.id.layout_header_github)
            tvNome = findViewById(R.id.github_tv_nome)
            tvLogin = findViewById(R.id.github_tv_login)
            tvBio = findViewById(R.id.github_tv_bio)
            cImagePhoto = findViewById(R.id.github_img_avatar)
        }
        header.isVisible = false

    }

    private fun getUserInfo() {
        val retrofitService = RetrofitService()
        val ownerApi = retrofitService.retrofit.create(OwnerApi::class.java)
        ownerApi.getUser("efabreu").enqueue(object : Callback<Owner> {
            override fun onResponse(call: Call<Owner>, response: Response<Owner>) {
                if (response.isSuccessful) {

                    response.body()?.let {
                        tvNome.text = it.name
                        tvLogin.text = it.login
                        tvBio.text = it.bio
                        LoadImageTask(cImagePhoto).execute(it.avatar_url)
                        header.isVisible = true
                    }

                }else{
                    Toast.makeText(context, "Erro ao conectar com Github.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Owner>, t: Throwable) {
                Log.d("Retrofit ->", t.message.toString())
            }

        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_github, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GithubFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}





