package com.alorma.rac.tv.program

import android.view.ViewGroup
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import androidx.lifecycle.Lifecycle
import coil.api.load
import com.alorma.rac.data.api.SectionApiModel
import com.alorma.rac.domain.model.Program

class ProgramSectionsPresenter(
    private val program: Program,
    private val lifecycle: Lifecycle
) : Presenter() {

    override fun onCreateViewHolder(parent: ViewGroup): Presenter.ViewHolder {
        val cardView = ImageCardView(parent.context)

        cardView.isFocusable = true
        cardView.isFocusableInTouchMode = true
        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any) {
        val cardView = viewHolder.view as ImageCardView

        val section = item as SectionApiModel
        cardView.titleText = section.title
        cardView.setMainImageDimensions(
            CARD_WIDTH,
            CARD_HEIGHT
        )

        program.images?.program?.let { image ->
            cardView.mainImageView.load(image) {
                lifecycle(lifecycle)
            }
        }
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        val cardView = viewHolder.view as ImageCardView
        // Remove references to images so that the garbage collector can free up memory
        cardView.badgeImage = null
        cardView.mainImage = null
    }

    companion object {
        private val CARD_WIDTH = 313
        private val CARD_HEIGHT = 176
    }
}