package br.com.efabreu.portifolioefabreu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.efabreu.portifolioefabreu.R
import br.com.efabreu.portifolioefabreu.model.Repository


class ReposAdapter(private val repos: List<Repository>) :
    RecyclerView.Adapter<ReposAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.repos_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = repos[position].name
        holder.description.text = repos[position].description
        //holder.html_url.text = repos[position].html_url
        holder.language.text = repos[position].language
    }


    override fun getItemCount(): Int = repos.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.item_repo_name)
        val description: TextView = view.findViewById(R.id.item_repo_descr)
        //val html_url: TextView = view.
        val language: TextView = view.findViewById(R.id.item_repo_lang)

    }


}