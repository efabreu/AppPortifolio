package br.com.efabreu.portifolioefabreu.adapter

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.efabreu.portifolioefabreu.R
import br.com.efabreu.portifolioefabreu.model.Repository
import com.google.android.material.color.MaterialColors.getColor
import com.squareup.picasso.Picasso


class ReposAdapter(private val repos: List<Repository>) :
    RecyclerView.Adapter<ReposAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.repos_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = repos[position].name
        holder.description.text = repos[position].description
        when(repos[position].language){
            "Kotlin" -> {
                holder.language.text = repos[position].language?.uppercase()
                holder.language.setTextColor(Color.WHITE)
                holder.language.setBackgroundColor(Color.BLUE)
            }
            "Java" -> {
                holder.language.text = repos[position].language?.uppercase()
                holder.language.setTextColor(Color.WHITE)
                holder.language.setBackgroundColor(Color.RED)
            }
            "C++" -> {
                holder.language.text = repos[position].language?.uppercase()
                holder.language.setTextColor(Color.WHITE)
                holder.language.setBackgroundColor(Color.GREEN)
            }
            else -> {
                holder.language.text = repos[position].language?.uppercase()
                holder.language.setTextColor(Color.WHITE)
                holder.language.setBackgroundColor(Color.GRAY)
            }
        }
        holder.url = repos[position].html_url

    }


    override fun getItemCount(): Int = repos.size

    class ViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        val name: TextView = view.findViewById(R.id.item_repo_name)
        val description: TextView = view.findViewById(R.id.item_repo_descr)
        val language: TextView = view.findViewById(R.id.item_repo_lang)
        //https://img.shields.io/badge/JAVA-red
        var url: String = ""

        override fun onClick(v: View) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            v.context.startActivity(intent)
        }
    }
}