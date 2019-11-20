package com.alorma.rac.core

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

open class BaseFragment(@LayoutRes layout: Int = 0) : Fragment(layout)