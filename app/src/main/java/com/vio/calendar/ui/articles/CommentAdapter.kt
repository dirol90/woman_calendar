package com.vio.calendar.ui.articles

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vio.calendar.R
import com.vio.calendar.data.article.model.Comment
import com.vio.calendar.inflate
import kotlinx.android.synthetic.main.item_comment.view.*



class CommentAdapter(private val comments: MutableList<Comment>) :
    RecyclerView.Adapter<CommentAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_comment))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(comments[position])
    }

    override fun getItemCount() = comments.size

    fun setComments(comments: List<Comment>) {
        this.comments.clear()
        this.comments.addAll(comments)
        notifyDataSetChanged()
    }

    fun addComment(comment: Comment) {
        this.comments.add(0, comment)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private lateinit var comment: Comment

        fun bind(comment: Comment) {
            this.comment = comment
            itemView.name.text = comment.userData.name

            try {
                itemView.circle.background.setColorFilter(
                    Color.parseColor(comment.userData.color),
                    PorterDuff.Mode.SRC_ATOP
                )
            } catch (iae: IllegalArgumentException) {
                // This color string is not valid
            }

            if (comment.userData.name.isNotEmpty())itemView.circleLetter.text = comment.userData.name[0].toUpperCase().toString()
            itemView.comment.text = comment.content
            itemView.commentDate.text = comment.createdAt.toString()
        }
    }

}
