package com.alorma.rac1.service

interface Playback {

    /**
     * Get the current [android.media.session.PlaybackState.getState]
     */
    /**
     * Set the latest playback state as determined by the caller.
     */
    var state: Int

    /**
     * @return boolean that indicates that this is ready to be used.
     */
    val isConnected: Boolean

    /**
     * @return boolean indicating whether the player is playing or is supposed to be
     * playing when we gain audio focus.
     */
    val isPlaying: Boolean

    /**
     * @return pos if currently playing an item
     */

    /**
     * Start/setup the playback.
     * Resources/listeners would be allocated by implementations.
     */
    fun start()

    /**
     * Stop the playback. All resources can be de-allocated by implementations here.
     * @param notifyListeners if true and a callback has been set by setCallback,
     * callback.onPlaybackStatusChanged will be called after changing
     * the state.
     */
    fun stop(notifyListeners: Boolean)

    /**
     * Queries the underlying stream and update the internal last known stream position.
     */
    fun updateLastKnownStreamPosition()

    fun play()

    fun pause()

    interface Callback {
        /**
         * On current music completed.
         */
        fun onCompletion()

        /**
         * on Playback status changed
         * Implementations can use this callback to update
         * playback state on the media sessions.
         */
        fun onPlaybackStatusChanged(state: Int)

        /**
         * @param error to be added to the PlaybackState
         */
        fun onError(error: String)
    }

    fun setCallback(callback: Callback)
}