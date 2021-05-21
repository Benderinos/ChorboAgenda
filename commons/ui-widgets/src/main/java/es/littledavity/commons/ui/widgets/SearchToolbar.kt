package es.littledavity.commons.ui.widgets

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import androidx.core.animation.doOnEnd
import androidx.core.view.isVisible
import com.paulrybitskyi.commons.ktx.endMargin
import com.paulrybitskyi.commons.ktx.getColor
import com.paulrybitskyi.commons.ktx.getDimension
import com.paulrybitskyi.commons.ktx.getDimensionPixelSize
import com.paulrybitskyi.commons.ktx.layoutInflater
import com.paulrybitskyi.commons.ktx.onClick
import com.paulrybitskyi.commons.ktx.setScale
import com.paulrybitskyi.commons.ktx.startMargin
import com.paulrybitskyi.commons.ktx.views.onTextChanged
import es.littledavity.commons.ui.extensions.hideKeyboard
import es.littledavity.commons.ui.extensions.showKeyboard
import es.littledavity.commons.ui.widgets.databinding.ViewSearchToolbarBinding

class SearchToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = ViewSearchToolbarBinding.inflate(context.layoutInflater, this)

    private lateinit var clearBtnValueAnimator: ValueAnimator

    private var isClearButtonVisible: Boolean
        set(value) { binding.clearBtnContainer.isVisible = value }
        get() = binding.clearBtnContainer.isVisible

    private var isSearchQueryEmpty = true

    var inputType: Int
        set(value) { binding.queryInputEt.inputType = value }
        get() = binding.queryInputEt.inputType

    var hintText: CharSequence
        set(value) { binding.queryInputEt.hint = value }
        get() = binding.queryInputEt.hint

    var searchQuery: CharSequence
        set(value) { binding.queryInputEt.setText(value) }
        get() = binding.queryInputEt.text

    var onQueryChanged: ((String) -> Unit)? = null
    var onSearchActionRequested: ((String) -> Unit)? = null

    var onBackButtonClicked: ((View) -> Unit)? = null
        set(value) {
            field = value
            binding.backBtnContainer.onClick { field?.invoke(it) }
        }

    var onClearButtonClicked: ((View) -> Unit)? = null

    init {
        initQueryInput()
        initClearButton()
        initDefaults()
    }

    private fun initQueryInput() {
        if(isSearchQueryEmpty) {
            hideClearButton(false)
        } else {
            showClearButton(false)
        }

        initQueryInputMargins()
        initQueryInputTextWatcher()
        initQueryInputActionListener()
    }

    private fun initQueryInputMargins() = with(binding.queryInputEt) {
        val buttonContainerSize = getDimensionPixelSize(R.dimen.toolbar_button_container_size)
        val horizontalMargin = getDimensionPixelSize(R.dimen.search_toolbar_query_input_horizontal_margin)
        val totalHorizontalMargin = (buttonContainerSize + horizontalMargin)

        startMargin = totalHorizontalMargin
        endMargin = totalHorizontalMargin
    }

    private fun initQueryInputTextWatcher() = with(binding) {
        queryInputEt.onTextChanged { query ->
            if(query.isNotEmpty()) {
                if(isSearchQueryEmpty) {
                    isSearchQueryEmpty = false
                    showClearButton(true)
                }
            } else {
                if(!isSearchQueryEmpty) {
                    isSearchQueryEmpty = true
                    hideClearButton(true)
                }
            }

            onQueryChanged?.invoke(query)
        }
    }

    private fun initQueryInputActionListener() {
        binding.queryInputEt.setOnEditorActionListener { _, actionId, _ ->
            if((actionId == EditorInfo.IME_ACTION_SEARCH) && searchQuery.isNotBlank()) {
                hideKeyboard()
                onSearchActionRequested?.invoke(searchQuery.toString())
            }

            true
        }
    }

    private fun initClearButton() {
        binding.clearBtnContainer.onClick {
            onClearButtonClicked?.invoke(it)
            clearQueryText()
            showKeyboard()
        }

        clearBtnValueAnimator = initClearButtonValueAnimator()
    }

    private fun initClearButtonValueAnimator(): ValueAnimator {
        return ValueAnimator.ofFloat().apply {
            addUpdateListener {
                binding.clearBtnContainer.setScale(it.animatedValue as Float)
            }

            interpolator = LinearInterpolator()
            duration = 100L
        }
    }

    private fun initDefaults() {
        elevation = getDimension(R.dimen.toolbar_elevation)
        setBackgroundColor(getColor(R.color.toolbar_background_color))
    }

    private fun clearQueryText() {
        binding.queryInputEt.setText("")
    }

    private fun showClearButton(animate: Boolean) {
        if(animate) {
            clearBtnValueAnimator.cancel()
            clearBtnValueAnimator.removeAllListeners()

            binding.clearBtnContainer.setScale(0f)
            isClearButtonVisible = true

            runClearButtonAnimation(0f, 1f)
        } else {
            isClearButtonVisible = true
        }
    }

    private fun hideClearButton(animate: Boolean) {
        if(animate) {
            clearBtnValueAnimator.cancel()
            clearBtnValueAnimator.removeAllListeners()
            clearBtnValueAnimator.doOnEnd { isClearButtonVisible = false }

            binding.clearBtnContainer.setScale(1f)

            runClearButtonAnimation(1f, 0f)
        } else {
            isClearButtonVisible = false
        }
    }

    private fun runClearButtonAnimation(vararg values: Float) {
        clearBtnValueAnimator.setFloatValues(*values)
        clearBtnValueAnimator.start()
    }

    fun showKeyboard(withDelay: Boolean = false) {
        binding.queryInputEt.showKeyboard(withDelay)
    }

    fun hideKeyboard() {
        binding.queryInputEt.hideKeyboard()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        clearBtnValueAnimator.cancel()
    }
}