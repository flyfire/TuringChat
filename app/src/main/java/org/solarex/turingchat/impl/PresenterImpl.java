package org.solarex.turingchat.impl;


import org.solarex.turingchat.bean.Msg;
import org.solarex.turingchat.utils.Logs;

import java.util.ArrayList;

public class PresenterImpl extends BasePresenter implements ModelImpl.ModelSaveCallback, HttpFetchResult.FetchCacllback{
    private ArrayList<Msg> mMsgs = null;

    ModelImpl mModelImpl;
    HttpFetchResult mHttpFetchResult;

    private static final String TAG = "PresenterImpl";

    public PresenterImpl(){
        mMsgs = new ArrayList<>();
        mModelImpl = new ModelImpl(this, mMsgs);
        mHttpFetchResult = new HttpFetchResult(this);
    }

    @Override
    public void onFetchSuccess(Msg message) {
        mModelImpl.saveMsg(message);
    }

    @Override
    public void onFetchFailure(Msg message) {
        mModelImpl.saveMsg(message);
    }

    @Override
    public void onSaveSuccess(ArrayList<Msg> mMsgs) {
        IView view = (IView)getView();
        Logs.d(TAG, "onSaveSuccess | view = " + view);
        if (view != null){
            view.updateUI(mMsgs);
        }
    }

    @Override
    public void sendMsg(String message) {
        Logs.d(TAG, "sendMsg | msg = " + message);
        mModelImpl.saveMsg(message);
        mHttpFetchResult.doFetch(message);
    }

    @Override
    public void initMsg(String message){
        mModelImpl.saveMsg(Msg.createFrom(Msg.TYPE_TEXT, message));
    }

    @Override
    public void detachView(){
        super.detachView();
        mHttpFetchResult.quitAllRequests();
        mModelImpl.clearMsg();
    }

    @Override
    public void showInputError(){
        IView view = (IView)getView();
        if (view != null){
            view.notifyInputError();
        }
    }

    public ArrayList<Msg> getMsgs(){
        return mMsgs;
    }
}
