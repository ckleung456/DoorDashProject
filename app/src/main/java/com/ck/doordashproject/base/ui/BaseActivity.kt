package com.ck.doordashproject.base.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ck.doordashproject.R
import com.ck.doordashproject.base.models.viewmodels.appnotification.AppNotificationViewModel

abstract class BaseActivity : AppCompatActivity() {
    companion object {
        private val TAG = BaseActivity::class.java.name
        private const val BACKSTACK_OFFSET = 2
    }

    var mCurrentFragment: Fragment? = null
    protected lateinit var appNotificationViewModel: AppNotificationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appNotificationViewModel = ViewModelProviders.of(this).get(AppNotificationViewModel::class.java)
        appNotificationViewModel.observeErrorNotification().observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
    }

    override fun onBackPressed() {
        val currentCount = supportFragmentManager.backStackEntryCount
        val shouldCloseCurrent = currentCount <= 0 || currentCount - 1 <= 0
        if (shouldCloseCurrent) {
            mCurrentFragment = getSelectedFragment(getEntryFragmentTag())
            super.onBackPressed()
        } else {
            val index = currentCount - BACKSTACK_OFFSET
            val backStackEntry = if (index >= 0) supportFragmentManager.getBackStackEntryAt(index) else null
            val tag = if (backStackEntry?.name.isNullOrEmpty())
                getEntryFragmentTag()
            else
                backStackEntry?.name!! //update current fragment tag back to previous tag
            if (isFragmentInBackStack(tag)) {
                supportFragmentManager.popBackStackImmediate(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
            switchFragment(getSelectedFragment(tag), tag)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        appNotificationViewModel.observeErrorNotification().removeObservers(this)
    }

    fun switchFragment(toFragment: Fragment, toFragmentTag: String) {
        if (toFragment != mCurrentFragment) {
            val ft = supportFragmentManager.beginTransaction()
            if (mCurrentFragment != null) {
                ft.hide(mCurrentFragment!!).addToBackStack(toFragmentTag)
            }
            ft.setCustomAnimations(
                R.anim.pull_in_right,
                R.anim.pull_out_left,
                R.anim.activity_close_enter,
                R.anim.activity_close_exit
            )
            if (toFragment.isAdded) {
                ft.show(toFragment)
            } else {
                ft.add(getContainerId(), toFragment, toFragmentTag)
            }
            ft.commit()
            mCurrentFragment = toFragment
        }
    }

    protected fun clearCurrentBackStacks() {
        var backStackCount = supportFragmentManager.backStackEntryCount
        while (backStackCount >= 0) {
            supportFragmentManager.popBackStackImmediate()
            backStackCount--
        }
        mCurrentFragment = null
    }

    fun isFragmentInBackStack(fragmentTag: String): Boolean {
        for (i in 0 until supportFragmentManager.backStackEntryCount) {
            if (fragmentTag == supportFragmentManager.getBackStackEntryAt(i).getName()) {
                return true
            }
        }
        return false
    }

    abstract fun getContainerId(): Int
    abstract fun getSelectedFragment(toTag: String): Fragment
    abstract fun getEntryFragmentTag(): String
}