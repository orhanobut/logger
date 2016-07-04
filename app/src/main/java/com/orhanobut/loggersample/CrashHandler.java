package com.orhanobut.loggersample;

import android.util.Log;

/**
 * @author Kale
 * @date 2016/7/1
 */

class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "CrashHandler";

    private static CrashHandler sInstance = new CrashHandler();

    //系统默认的异常处理（默认情况下，系统会终止当前的异常程序）  
    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;

    static CrashHandler getInstance() {
        return sInstance;
    }

    //这里主要完成初始化工作  
    void init() {
        //获取系统默认的异常处理器  
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        //将当前实例设为系统默认的异常处理器  
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 这个是最关键的函数，当程序中有未被捕获的异常，系统将会自动调用#uncaughtException方法
     * thread为出现未捕获异常的线程，ex为未捕获的异常，有了这个ex，我们就可以得到异常信息。
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        //这里可以通过网络上传异常信息到服务器，便于开发人员分析日志从而解决bug  
        uploadExceptionToServer(thread, ex);
        //打印出当前调用栈信息  
        ex.printStackTrace();

        //如果系统提供了默认的异常处理器，则交给系统去结束我们的程序，否则就由我们自己结束自己  
        if (mDefaultCrashHandler != null) {
            mDefaultCrashHandler.uncaughtException(thread, ex);
        }

    }

    private void uploadExceptionToServer(Thread thread, Throwable ex) {
        Log.d(TAG, "======================================== ");
        System.out.println("                                                         .;jLGEEEEEEEEEEEEEEEt                  \n"
                + "                                                              .;jLGEEEEEEEEEEEEEEEt                 \n"
                + "                                                               .;jLGEEEEEEEEEEEEEEEi                \n"
                + "                                                                .;jLDEEEEEEEEEEEEEEEt               \n"
                + "                                                                 .;jLGEEEEEEEEEEEEEEEi              \n"
                + "                                                                  :;jLGEEEEEEEEEEEEEEE;             \n"
                + "                                                                   :;jLDEEEEEEEEEEEEEEEi            \n"
                + "                                                                    :;jLGEEEEEEEEEEEEEEE;           \n"
                + "                                                                     :;jLDEEEEEEEEEEEEEEE,          \n"
                + "                                                                      :;jLDEEEEEEEEEEEEEEE;         \n"
                + "                                                                       :;jLDEEEEEEEEEEEEEEE,        \n"
                + "                                                                        :;jLDEEEEEEEEEEEEEEE,       \n"
                + "                                                                         :;jLDEEEEEEEEEEEEEEE;      \n"
                + "                                                                          :;jLDEEEEEEEEEEEEEEE:     \n"
                + "                                                                           :;jLDEEEEEEEEEEEEEEE.    \n"
                + "                                                                            :;jLDEEEEEEEEEEEEEEE,   \n"
                + "                                                                             .,tLGEEEEEEEEEEEEEEEL  \n"
                + "                                                                              .,tLGEEEEEEEEEEEEEEEf \n"
                + "              .D##                                                             .,tLGEEEEEEEEEEEEEEEL\n"
                + "           ,W#####                                                              .,tLGEEEEEEEEEEEEEEE\n"
                + "        t#########                                                               .,tLGEEEEEEEEEEEEEE\n"
                + "     L############                                                                .,tLGEEEEEEEEEEEEE\n"
                + "     EG, j########                                                                 .,tLGEEEEEEEEEEEE\n"
                + "          ;#######                                                                  .,tLGEEEEEEEEEEE\n"
                + "           #######                                                                   .,tLGEEEEEEEEEE\n"
                + "           #######                                                                    .,tLGEEEEEEEEE\n"
                + "           #######                                                                     .;tLGEEEEEEEE\n"
                + "           #######                                                                      .;jLGEEEEEEE\n"
                + "           #######                                                                       .;jLGEEEEEE\n"
                + "           #######                                                                        .;jLGEEEEE\n"
                + "           #######                                                                         .;jLGEEEE\n"
                + "           #######                                                                          .;jLGEEE\n"
                + "           #######                                                                           .;jLGEE\n"
                + "           #######                                                                            .;jLGE\n"
                + "           #######                                                                             .;jLG\n"
                + "           #######          #################                                                   .;tj\n"
                + "           #######          ;j###########Ef;;                                                    .,,\n"
                + "           #######             #######i                                                           ..\n"
                + "           #######             #####;                                                               \n"
                + "           #######            ####W                                                                 \n"
                + "           #######          ;####t                                                                  \n"
                + "           #######         E####                                    #####                           \n"
                + "           #######        ####K                                    f#####                           \n"
                + "           #######      t####;                                     #####E                           \n"
                + "           #######     W####                                       #####t                           \n"
                + "           #######   .####D                                        #####                            \n"
                + "           #######  f####,                                         #####                            \n"
                + "           ####### ######                         L#######L        #####        ;#######j           \n"
                + "           ###############                      ,###########j      #####       ###########.         \n"
                + "           #######K########                     #############j     #####     .#############         \n"
                + "           ####### K########                    ####t  .D#####     ####K     #####j  ,#####j        \n"
                + "           #######  K########                   E##      ;####j    ####D    W####      ####W        \n"
                + "           #######   W########                   tf       ####D    ####D    ####:      ####W        \n"
                + "           #######    #########                         iW#####    ####D   L####     .W####t        \n"
                + "           #######     #########                    i##########    ####D   #####  ;D#######         \n"
                + "           #######      #########                 ;############    ####D   ##############K          \n"
                + "           #######       #########               D#####KjE#####    #####   ############K.           \n"
                + "           #######        #########             L####G    #####    #####   #########G,              \n"
                + "           #######         #########            ####D     #####    #####   ######L                  \n"
                + "           #######          #########          j####      #####    #####   #####;         tG        \n"
                + "           #######           #########         D####;     #####    #####   E####;         ##        \n"
                + "           #######            #########        j#####.    #####    #####   .#####j      ;##D        \n"
                + "          .#######            ;#########;       #######DD######    #####;   W#######DD#####:        \n"
                + "          ########W           .###########:     t##############    #####L    #############K         \n"
                + "     ;;fW###########Gi;;    ;t#################  .W############;   ######     W##########E          \n"
                + "     ###################    ###################     ;jD#####DDj    i#####      ,W######K.           ");
        
        Log.d(TAG, "thread name: " + thread.getName());
        Log.d(TAG, "throwable msg: " + ex.getMessage());
        Log.d(TAG, "======================================== ");
    }

}  