package com.alorma.rac.tv.program

import android.view.ViewGroup
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import androidx.lifecycle.Lifecycle
import coil.api.load
import com.alorma.rac.data.api.Program
import com.alorma.rac.data.api.Section

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

        val section = item as Section
        cardView.titleText = section.title
        cardView.setMainImageDimensions(
            CARD_WIDTH,
            CARD_HEIGHT
        )

        cardView.mainImageView.load(program.images.program) {
            lifecycle(lifecycle)
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