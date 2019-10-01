package com.gon.kineapp.ui.adapters;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * @Fede Manage saveInstance and restoreInstance state as FragmentStatePagerAdapter and can retrieve reference to fragment
 * added with the fragment manager. This way we avoid call getItem() everyTime in onPageSelected and reuse a created fragment
 * if it is allowed.
 * See : getFragmentForPosition()
 */
public abstract class CustomFragmentStatePagerAdapter extends PagerAdapter {

  protected final String ARGS_TAG_KEY = "args";

  private static final String TAG = "FragmentPagerAdapter";

  private final FragmentManager mFragmentManager;
  private FragmentTransaction mCurTransaction = null;

  private Fragment mCurrentPrimaryItem = null;
  private ArrayList<Fragment> mFragments = new ArrayList<>();
  private ArrayList<Fragment.SavedState> mSavedState = new ArrayList<>();


  public CustomFragmentStatePagerAdapter(FragmentManager fm) {
    mFragmentManager = fm;
  }

  /**
   * Return the Fragment associated with a specified position.
   */
  public abstract Fragment getItem(int position);

  @Override
  public void startUpdate(ViewGroup container) {
  }

  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    // If we already have this item instantiated, there is nothing
    // to do.  This can happen when we are restoring the entire pager
    // from its saved state, where the fragment manager has already
    // taken care of restoring the fragments we previously had instantiated.
    if (mFragments.size() > position) {
      Fragment f = mFragments.get(position);
      if (f != null) {
        return f;
      }
    }

    if (mCurTransaction == null) {
      mCurTransaction = mFragmentManager.beginTransaction();
    }

    Fragment fragment = getItem(position);

    if (mSavedState.size() > position) {
      Fragment.SavedState fss = mSavedState.get(position);
      if (fss != null) {
        fragment.setInitialSavedState(fss);
      }
    }

    while (mFragments.size() <= position) {
      mFragments.add(null);
    }

    fragment.setMenuVisibility(false);
    fragment.setUserVisibleHint(false);
    mFragments.set(position, fragment);

    Bundle arguments = fragment.getArguments();
    String fragmentTag;
    if (arguments != null) {
      fragmentTag = arguments.getString(ARGS_TAG_KEY);
    } else {
      fragmentTag = position + "";
    }

    mCurTransaction.add(container.getId(), fragment, fragmentTag);
    return fragment;
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    Fragment fragment = (Fragment) object;

    if (mCurTransaction == null) {
      mCurTransaction = mFragmentManager.beginTransaction();
    }

    while (mSavedState.size() <= position) {
      mSavedState.add(null);
    }

    mSavedState.set(position, mFragmentManager.saveFragmentInstanceState(fragment));
    mFragments.set(position, null);
    mCurTransaction.remove(fragment);
  }

  @Override
  public void setPrimaryItem(ViewGroup container, int position, Object object) {
    Fragment fragment = (Fragment) object;
    if (fragment != mCurrentPrimaryItem) {
      if (mCurrentPrimaryItem != null) {
        mCurrentPrimaryItem.setMenuVisibility(false);
        mCurrentPrimaryItem.setUserVisibleHint(false);
      }
      if (fragment != null) {
        fragment.setMenuVisibility(true);
        fragment.setUserVisibleHint(true);
      }
      mCurrentPrimaryItem = fragment;
    }
  }

  @Override
  public void finishUpdate(ViewGroup container) {
    if (mCurTransaction != null) {
      mCurTransaction.commitAllowingStateLoss();
      mCurTransaction = null;
      mFragmentManager.executePendingTransactions();
    }
  }

  @Override
  public boolean isViewFromObject(View view, Object object) {
    return ((Fragment) object).getView() == view;
  }


  @Override
  public Parcelable saveState() {
    Bundle state = null;
    if (mSavedState.size() > 0) {
      state = new Bundle();
      Fragment.SavedState[] fss = new Fragment.SavedState[mSavedState.size()];


      ArrayList<Fragment.SavedState> savedStates = new ArrayList<>();
      ArrayList<String> savedKeys = new ArrayList<>();

      savedStates.toArray(fss);

      state.putStringArrayList("keys", savedKeys);
      state.putParcelableArray("states", fss);
    }

    for (int i = 0; i < mFragments.size(); i++) {
      Fragment f = mFragments.get(i);
      if (f != null) {
        if (state == null) {
          state = new Bundle();
        }
        String key = "f" + i;
        mFragmentManager.putFragment(state, key, f);
      }
    }
    return state;
  }

  @Override
  public void restoreState(Parcelable state, ClassLoader loader) {
    if (state != null) {
      Bundle bundle = (Bundle) state;
      bundle.setClassLoader(loader);

      Parcelable[] fss = bundle.getParcelableArray("states");

      mSavedState.clear();
      mFragments.clear();
      if (fss != null) {
        for (int i = 0; i < fss.length; i++) {
          Parcelable fs = fss[i];
          mSavedState.add((Fragment.SavedState) fs);
        }
      }

      Iterable<String> keys = bundle.keySet();
      for (String key : keys) {
        if (key.startsWith("f")) {
          int index = Integer.parseInt(key.substring(1));
          Fragment f = mFragmentManager.getFragment(bundle, key);
          if (f != null) {
            while (mFragments.size() <= index) {
              mFragments.add(null);
            }
            f.setMenuVisibility(false);
            mFragments.set(index, f);
          } else {
            Log.w(TAG, "Bad fragment at key " + key);
          }
        }
      }
    }
  }

  @Nullable
  public Fragment getFragmentForTag(String fragmentTag) {
    return mFragmentManager.findFragmentByTag(fragmentTag);
  }
}