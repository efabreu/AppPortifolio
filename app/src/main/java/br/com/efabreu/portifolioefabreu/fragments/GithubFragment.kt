package br.com.efabreu.portifolioefabreu.fragments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.icu.lang.UProperty.INT_START
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.AsyncTask
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.efabreu.portifolioefabreu.R
import br.com.efabreu.portifolioefabreu.adapter.ReposAdapter
import br.com.efabreu.portifolioefabreu.data.OwnerApi
import br.com.efabreu.portifolioefabreu.data.RepositoriesApi
import br.com.efabreu.portifolioefabreu.data.RetrofitService
import br.com.efabreu.portifolioefabreu.model.Owner
import br.com.efabreu.portifolioefabreu.model.Repository
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

    private lateinit var header :RelativeLayout
    private lateinit var tvNome :TextView
    private lateinit var tvLogin :TextView
    private lateinit var tvBio :TextView
    private lateinit var cImagePhoto :CircleImageView
    private lateinit var recViewRepos :RecyclerView
    private lateinit var noInternet :ImageView
    private lateinit var progressBar :ProgressBar



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
        getRepositories()

    }
    override fun onResume() {
        super.onResume()
        if (checkForInternet(context)) {
            getUserInfo()
            getRepositories()
        } else {
            emptyState()
        }
    }

    private fun emptyState() {

        progressBar.isVisible = false
        header.isVisible = false
        recViewRepos.isVisible = false
        noInternet.isVisible = true
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
            recViewRepos = findViewById(R.id.rv_repositories)
            progressBar = findViewById(R.id.github_progressBar)
            noInternet = findViewById(R.id.github_nointernet)
        }
        progressBar.isVisible = true
        header.isVisible = false
        recViewRepos.isVisible = false


    }

    fun setupReposList(lista :List<Repository>){

        val reposAdapter = ReposAdapter(lista)
        recViewRepos.layoutManager = LinearLayoutManager(context)
        recViewRepos.apply {
            isVisible = true
            adapter = reposAdapter
        }

    }

    private fun getUserInfo() {
        val retrofitService = RetrofitService()
        val ownerApi = retrofitService.retrofit.create(OwnerApi::class.java)
        ownerApi.getUser("efabreu").enqueue(object : Callback<Owner> {
            override fun onResponse(call: Call<Owner>, response: Response<Owner>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        tvNome.text = it.name
                        val usernameStr = SpannableStringBuilder("Username: ${it.login}")
                        usernameStr.setSpan(
                            StyleSpan(Typeface.BOLD),
                            0,
                            9,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        tvLogin.text = usernameStr
                        val bioStr = SpannableStringBuilder("Bio: ${it.bio}")
                        bioStr.setSpan(
                            StyleSpan(Typeface.BOLD),
                            0,
                            4,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        tvBio.text = bioStr
                        LoadImageTask(cImagePhoto).execute(it.avatar_url)
                        noInternet.isVisible = false
                        header.isVisible = true
                        progressBar.isVisible = false
                        recViewRepos.isVisible = true
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
    private fun getRepositories(){
        val retrofitService = RetrofitService()
        val reposApi = retrofitService.retrofit.create(RepositoriesApi::class.java)
        reposApi.getRepos("efabreu").enqueue(object :Callback<List<Repository>> {
            override fun onResponse(
                call: Call<List<Repository>>,
                response: Response<List<Repository>>
            ) {
                if(response.isSuccessful){
                    response.body()?.let {
                        setupReposList(it)
                    }
                }
            }

            override fun onFailure(call: Call<List<Repository>>, t: Throwable) {
                Log.d("Retrofit ->", t.message.toString())
            }

        })
    }
    fun checkForInternet(context: Context?): Boolean {
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }

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








