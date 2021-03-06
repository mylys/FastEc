package com.zhaoman.manny_ec.lanucher;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.zhaoman.manny_core.app.AccountManager;
import com.zhaoman.manny_core.app.IUserChecker;
import com.zhaoman.manny_core.delegates.MannyDelegate;
import com.zhaoman.manny_core.ui.launcher.ILauncherListener;
import com.zhaoman.manny_core.ui.launcher.LauncherHolderCreator;
import com.zhaoman.manny_core.ui.launcher.OnLauncherFinishTag;
import com.zhaoman.manny_core.ui.launcher.ScrollLanucherTag;
import com.zhaoman.manny_core.utils.storage.MannyPreference;
import com.zhaoman.manny_ec.R;

import java.util.ArrayList;

/**
 * Author:zhaoman
 * Date:2018/11/10
 * Description:
 */
public class LauncherScrollDelegate extends MannyDelegate implements OnItemClickListener {


    private ConvenientBanner<Integer> mConvenientBanner=null;
    private ArrayList<Integer> INTEGERS=new ArrayList<>();

    private ILauncherListener mILauncherListener=null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ILauncherListener) mILauncherListener = (ILauncherListener) activity;
    }


    private void initBanner(){
        INTEGERS.add(R.mipmap.launcher_01);
        INTEGERS.add(R.mipmap.launcher_02);
        INTEGERS.add(R.mipmap.launcher_03);
        INTEGERS.add(R.mipmap.launcher_04);
        INTEGERS.add(R.mipmap.launcher_05);
        mConvenientBanner.setPages(new LauncherHolderCreator(),INTEGERS)
                .setPageIndicator(new int[]{R.drawable.dot_normal,R.drawable.dot_focus})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setOnItemClickListener(this)
                .setCanLoop(false);
    }


    @Override
    public Object setLayout() {
        mConvenientBanner=new ConvenientBanner<>(getContext());
        return mConvenientBanner;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

        initBanner();
    }

    @Override
    public void onItemClick(int position) {

        //如果点击的是最后一个
        if (position==INTEGERS.size()-1){
            //保存已经进入过app 的标志
            MannyPreference.setAppFlag(ScrollLanucherTag.HAS_FIRST_LAUNCHER_APP.name(),true);

            // TODO: 2018/11/10  检查是否已经登录
            AccountManager.checkAccount(new IUserChecker() {
                @Override
                public void onSignIn() {
                    if (mILauncherListener!=null){

                        mILauncherListener.onLanucherFinish(OnLauncherFinishTag.SIGNED);
                    }

                }

                @Override
                public void onNotSignIn() {
                    if (mILauncherListener!=null){

                        mILauncherListener.onLanucherFinish(OnLauncherFinishTag.NOT_SIGNED);
                    }
                }
            });
        }
    }
}
