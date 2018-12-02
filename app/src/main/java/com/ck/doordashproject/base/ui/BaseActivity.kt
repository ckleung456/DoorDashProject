package com.ck.doordashproject.base.ui

import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.ck.doordashproject.R

abstract class BaseActivity: AppCompatActivity() {
    companion object {
        private val TAG = BaseActivity::class.java.name
        private const val BACKSTACK_OFFSET = 2
    }
    var mVisibleFragment: Fragment? = null
    var mVisibleFragmentTag: String? = ""

    fun switchFragment(fromFragment: Fragment?, toFragment: Fragment, toFragmentTag: String) {
        if (TextUtils.isEmpty(mVisibleFragmentTag) || !TextUtils.equals(mVisibleFragmentTag, toFragmentTag)) {
            mVisibleFragmentTag = toFragmentTag
            setSelectedFragment()
            val ft = supportFragmentManager.beginTransaction()
            if (fromFragment != null) {
                if (isFragmentInBackStack(toFragmentTag)) {
                    supportFragmentManager.popBackStackImmediate(toFragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                }
                ft.hide(fromFragment).addToBackStack(toFragmentTag)
            }
            ft.setCustomAnimations(R.anim.pull_in_right, R.anim.pull_out_left, R.anim.activity_close_enter, R.anim.activity_close_exit)
            try {
                if (toFragment.isAdded) {
                    ft.show(toFragment)
                } else {
                    ft.add(getContainerId(), toFragment, toFragmentTag)
                }
                ft.commitAllowingStateLoss()
            } catch (e: Exception) {
                Log.e(TAG, "Unexcepted error: " + e.message!!, e)
            }
        }
    }

    fun handleBackPressed(): Boolean {
        val currentCount = supportFragmentManager.getBackStackEntryCount()
        val shouldCloseCurrent = currentCount <= 0 || currentCount - 1 <= 0
        if (shouldCloseCurrent) {
            mVisibleFragmentTag = getEntryFragmentTag()
        } else {
            val index = currentCount - BACKSTACK_OFFSET
            val backStackEntry = supportFragmentManager.getBackStackEntryAt(index)
            mVisibleFragmentTag = if (TextUtils.isEmpty(backStackEntry.getName()))
                getEntryFragmentTag()
            else
                backStackEntry.getName() //update current fragment tag back to previous tag
        }
        setSelectedFragment()
        return !shouldCloseCurrent && supportFragmentManager.popBackStackImmediate()
    }

    protected fun clearCurrentBackStacks() {
        var backStackCount = supportFragmentManager.getBackStackEntryCount()
        while (backStackCount >= 0) {
            supportFragmentManager.popBackStackImmediate()
            backStackCount--
        }
        mVisibleFragment = null
    }

    fun isFragmentInBackStack(fragmentTag: String): Boolean {
        for (i in 0 until supportFragmentManager.getBackStackEntryCount()) {
            if (fragmentTag == supportFragmentManager.getBackStackEntryAt(i).getName()) {
                return true
            }
        }
        return false
    }

    abstract fun getContainerId(): Int
    abstract fun setSelectedFragment()
    abstract fun getEntryFragmentTag(): String
}