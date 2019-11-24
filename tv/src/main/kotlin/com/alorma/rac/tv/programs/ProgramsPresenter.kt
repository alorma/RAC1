package com.alorma.rac.tv.programs

import android.view.ViewGroup
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import androidx.lifecycle.Lifecycle
import coil.Coil
import coil.api.load
import coil.size.OriginalSize
import coil.size.PixelSize
import com.alorma.rac.domain.model.Program
import com.alorma.rac.extension.dp
import com.alorma.rac.tv.R

/**
 * A CardPresenter is used to generate Views and bind Objects to them on demand.
 * It contains an ImageCardView.
 */
class ProgramsPresenter(
    private val lifecycle: Lifecycle
) : Presenter() {
    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val cardView = ImageCardView(parent.context)

        cardView.isFocusable = true
        cardView.isFocusableInTouchMode = true
        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        val cardView = viewHolder.view as ImageCardView

        val program = item as Program
        cardView.titleText = program.title
        cardView.contentText = program.subtitle
        cardView.setMainImageDimensions(
            CARD_WIDTH,
            CARD_HEIGHT
        )

        program.images?.program?.let {
            cardView.mainImageView.load(it) {
                size(PixelSize(CARD_WIDTH.dp, CARD_HEIGHT.dp))
                lifecycle(lifecycle)
            }
        }

        if (program.now) {
            Coil.load(cardView.context, R.drawable.ic_now) {
                size(OriginalSize)
                target { cardView.badgeImage = it }
            }
        } else {
            cardView.badgeImage = null
        }
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        val cardView = viewHolder.view as ImageCardView
        // Remove references to images so that the garbage collector can free up memory
        cardView.badgeImage = null
        cardView.mainImage = null
    }

    companion object {
        private const val CARD_WIDTH = 313
        private const val CARD_HEIGHT = 176
    }
}
