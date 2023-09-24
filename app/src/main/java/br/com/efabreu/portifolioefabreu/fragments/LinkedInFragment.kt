package br.com.efabreu.portifolioefabreu.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import br.com.efabreu.portifolioefabreu.R


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val URL_LINKEDIN = "https://www.linkedin.com/in/efreitasdeabreu/"

class LinkedInFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var webView: WebView
    private lateinit var noInternet : ImageView
    private lateinit var progressBar : ProgressBar

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

    }

    override fun onResume() {
        super.onResume()

        if (checkForInternet(context)) {
            setupWebview()
        } else {
            emptyState()
        }

    }

    private fun emptyState() {
        webView.isVisible = false
        progressBar.isVisible = false
        noInternet.isVisible = true
    }

    private fun setupWebview() {
        webView.webViewClient = WebViewClient()
        webView.apply {
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
            settings.javaScriptEnabled = true
            //settings.domStorageEnabled = true
        }
        webView.loadUrl(URL_LINKEDIN)
        webView.isVisible = true
        progressBar.isVisible = false
        noInternet.isVisible = false
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

    private fun setupScreen(view :View){
        view.apply {
            progressBar = findViewById(R.id.linkedin_progressBar)
            noInternet = findViewById(R.id.linkedin_nointernet)
            webView = findViewById(R.id.linkedin_webview)
        }

        webView.isVisible = false
        progressBar.isVisible = true
        noInternet.isVisible = false


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_linked_in, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LinkedInFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}