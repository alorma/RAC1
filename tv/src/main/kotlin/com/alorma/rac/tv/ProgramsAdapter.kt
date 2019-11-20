package com.alorma.rac.tv

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import androidx.lifecycle.Lifecycle
import coil.api.load
import coil.size.PixelSize
import com.alorma.rac.data.api.Program
import com.alorma.rac.extension.dp
import kotlin.properties.Delegates

/**
 * A CardPresenter is used to generate Views and bind Objects to them on demand.
 * It contains an ImageCardView.
 */
class ProgramsAdapter(
    private val lifecycle: Lifecycle
) : Presenter() {
    private var mDefaultCardImage: Drawable? = null
    private var sSelectedBackgroundColor: Int by Delegates.notNull()
    private var sDefaultBackgroundColor: Int by Delegates.notNull()

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        sDefaultBackgroundColor = ContextCompat.getColor(parent.context, R.color.default_background)
        sSelectedBackgroundColor =
            ContextCompat.getColor(parent.context, R.color.selected_background)
        mDefaultCardImage = ContextCompat.getDrawable(parent.context, R.drawable.movie)

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

        cardView.mainImageView.load(program.images.program) {
            lifecycle(lifecycle)
            size(PixelSize(CARD_WIDTH.dp, CARD_HEIGHT.dp))
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
