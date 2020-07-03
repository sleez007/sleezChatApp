package ng.novacore.sleezchat.views

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import androidx.viewpager.widget.ViewPager

class NovaPager : ViewPager {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    // Touch event variables
    private val mTouchable: ArrayList<View> = ArrayList()
    private var mTouchOverrideView: View? = null
    //private var mTouchOverrideTime: Long = -1

    // Reveal transformation variables
    private var mRevealPosition: Int = -1
    private var appBar: View? = null

    private var mTab: View? = null

    private var offsetChangedListener:OnOffsetChangedListener? = null

    // State variables holding current states of full screen and translucence
    private var mState = SCROLL_STATE_IDLE
    private var mCurrent = -1
    private var shouldTransform = false
    private var isStateSaved = false

    // Init the reveal transformer here
    private var mTransformer: RevealTransformer? = null

    init {
        mTransformer = RevealTransformer()
        // bind the reveal transformer to the ViewPager
        // bind the reveal transformer to the ViewPager
        addOnPageChangeListener(mTransformer!!)


        // Intercept touch events and deliver to other view layers beneath it when in reveal state
        setOnTouchListener { _, event -> // deliver touch events only when in IDLE state and on reveal position
            if (currentItem == mRevealPosition && mState == SCROLL_STATE_IDLE) {

                // Offset the vertical position of the touch event
                // due to a displacement of the ViewPager caused by the AppBar collapsing.
                event.offsetLocation(0f, appBar!!.top.toFloat())

                // check if any view has a hold on touch events
                if (mTouchOverrideView != null) {
                    mTouchOverrideView!!.dispatchTouchEvent(event)
                } else {
                    // Keep sending the touch event layer by layer until a view consumes it
                    for (touchable in mTouchable) {
                        if (touchable.dispatchTouchEvent(event)) break
                    }
                }
            }
            false
        }

    }

    /**
     * Sets the position of the revealing item fragment.
     */
    fun setRevealPosition(position: Int) {
        mRevealPosition = position
    }


    /**
     * Used to set views that can receive touch events behind the view pager
     *
     * @param view       The view to receive the touch event
     * @param layerIndex Touch events are delivered in ascending order from index 0 till a view
     * completely consumes the event
     */
    fun addTouchableViewLayer(view: View?, layerIndex: Int) {
        var index: Int = resolveIndex(layerIndex)
        if (!mTouchable.contains(view)) {
            mTouchable.add(index, view!!)
        } else if (mTouchable.indexOf(view) != index) {
            mTouchable.remove(view)
            index = resolveIndex(index)
            mTouchable.add(index, view!!)
        }
    }

    fun removeTouchableViewLayer(view: View?): Boolean {
        return mTouchable.remove(view)
    }

    fun removeTouchableViewLayerIndex(index: Int) {
        if (index >= 0 && index < mTouchable.size) mTouchable.removeAt(index)
    }


    fun setOnOffsetChangedListener(listener: OnOffsetChangedListener) {
        offsetChangedListener = listener
    }

    private fun resolveIndex(index: Int): Int {
        if (mTouchable.size == 0 || index < 0) return 0
        return if (index > mTouchable.size) mTouchable.size else index
    }

    // Individual methods for binding views that would be transformed during state changes
    fun bindViews(appBar: View) {
        this.appBar = appBar
        touchables.remove(mTab)
        addTouchableViewLayer(appBar, 1)
        mTab = appBar
        setRevealPosition(0)
    }

    /**
     * This method should not be called before any call to [ViewPager.setCurrentItem] is made during
     * layout initialization.
     * A call to [ViewPager.setCurrentItem] after calling this method during app Initialization,
     * introduces a subtle transformation error.
     *
     * @param inState   . Pass the savedInstanceState bundle from [Activity.onCreate] to this method
     * @param firstInit . Set to false if the index of the  tab, [mRevealPosition], = 0
     * and you do not want to initialise layout at that position. It disables initial transformations.
     * Default value is set to false. Set true if initial transformation is required
     */
    fun initTransformer(inState: Bundle?, firstInit: Boolean) {
        var current: Int
        current = inState?.getInt("RevealTransformer.mCurrent", -2) ?: 0
        isStateSaved = current != -2
        if (mCurrent != -1 && inState == null) current = mCurrent
        if (inState == null && current == mRevealPosition && firstInit ||
            inState != null && current == mRevealPosition
        ) {
            mCurrent = current
            mTransformer!!.swipe()
        }
    }

    /**
     * Method should be called in the [Activity.onSaveInstanceState] method to save the current state
     * of the UI
     */
    fun saveState(outState: Bundle) {
        outState.putInt("RevealTransformer.mCurrent", mCurrent)
    }


    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)

        // check if user saved view state in activity or not then initialise transformer
        if (!isStateSaved) initTransformer(null, true)
    }


    /**
     * The page transform class that handles transformation of the appbar, swipeForeground,
     * action button and state of translucence and full screen.
     **/
    inner class RevealTransformer(): OnPageChangeListener {
        // Initialises first transformation for the swipe view
        fun swipe() {
            Thread(Runnable {

                // get the width of the screen
                val w = width

                // get an instance of the bottom value of the appBar
                var h = appBar!!.bottom

                // Loop till view is drawn or transformation is not necessary
                while (h == 0 && appBar != null && appBar!!.visibility == View.VISIBLE ||
                    w == 0 && visibility == View.VISIBLE
                ) h = appBar!!.bottom
                val finalH = h
                val handler = Handler(context.applicationContext.mainLooper)
                synchronized(1){
                    handler.post {
                        // Translate the appBar up as the swipeReveal slides into view
                        appBar!!.translationY = -finalH - finalH / 8f
                    }
                }

            }).start()
            mCurrent = currentItem
        }


        override fun onPageScrollStateChanged(state: Int) {
            if (state == SCROLL_STATE_DRAGGING || state == SCROLL_STATE_IDLE) {
                shouldTransform = true
            }

            mState = state
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            offsetChangedListener?.onOffsetChanged(positionOffset)

            // going right offset changes from zero to one
            val w = width

            // get an instance of the bottom value of the appBar

            // get an instance of the bottom value of the appBar
            val h = (appBar?.bottom ?: 0)

            if (position == mRevealPosition && shouldTransform) {

                // Translate the appBar up as the swipeReveal slides into view
                var y: Float = (positionOffset * h) - h
                if (y == -h.toFloat()) y = -h - h / 8f
                appBar!!.translationY = y

/*
                // Set translucence based on the position of the swipeReveal
                if (positionOffset < 1) setTranslucentNav();
                else exitTranslucentNav();

                // Set full screen based on the position of swipeReveal
                if (positionOffset > 0) exitFullScreen();
                else setFullScreen();*/
            }else if (position == mRevealPosition - 1 && shouldTransform) {

                // going right offset changes from zero to one
                // Translate the appBar up as the swipeReveal slides into view
                appBar!!.translationY = -(positionOffset * h)
            }else{
                if (appBar!!.translationY != 0f /*|| swipeForeground.getTranslationX() == 0f*/) {
                    //   if (appBar.getTranslationY() != 0f) exitTranslucentNav();
                    // Reset translation of the appBar
                    appBar!!.translationY = 0f
                }
            }
        }

        override fun onPageSelected(position: Int) {
            // check if page scroll state is settling from/to the swipeReveal position


            // check if page scroll state is settling from/to the swipeReveal position
            shouldTransform = mCurrent == mRevealPosition || position == mRevealPosition


            mCurrent = position
        }

    }

    /**
     * Interface for listening to when the swiping view becomes visible and when it looses visibility
     */
    interface OnOffsetChangedListener {
        fun onOffsetChanged(var2: Float)
    }

}