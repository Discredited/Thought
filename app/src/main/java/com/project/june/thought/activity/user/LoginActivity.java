package com.project.june.thought.activity.user;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.project.june.thought.R;
import com.project.june.thought.base.BaseActivity;
import com.project.june.thought.model.UserEntry;
import com.project.june.thought.model.UserTable;
import com.project.xujun.juneutils.customview.JuneButton;

import org.xutils.JuneToolsApp;
import org.xutils.db.Selector;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import butterknife.InjectView;

public class LoginActivity extends BaseActivity {

    @InjectView(R.id.user_account)
    EditText user_account;
    @InjectView(R.id.user_password)
    EditText user_password;
    @InjectView(R.id.login_button)
    JuneButton login_button;
    private String account;
    private String password;

    public static void startThis(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected int getTempFragmentId() {
        return 0;
    }

    @Override
    protected void logicProgress() {
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account = user_account.getText().toString();
                password = user_password.getText().toString();

                if (null == account || "".equals(account)){
                    Toast.makeText(mActivity, "账号不能为空", Toast.LENGTH_SHORT).show();
                }else if (null == password || "".equals(password)){
                    Toast.makeText(mActivity, "密码不能为空", Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        Selector<UserTable> selector = JuneToolsApp.getDbManager().selector(UserTable.class);
                        WhereBuilder wb = WhereBuilder.b().and("account","=",account);
                        selector.where(wb);
                        UserTable userTable = selector.findFirst();

                        if (null == userTable){
                            Toast.makeText(mActivity, "账号或者密码错误", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(mActivity, "登录成功", Toast.LENGTH_SHORT).show();
                            UserEntry userEntry = new UserEntry();
                            userEntry.setId(userTable.getId());
                            userEntry.setName(userTable.getName());
                            userEntry.setAvatar(userTable.getAvatar());
                            userEntry.setAccount(userTable.getAccount());
                            userEntry.setPassword(userTable.getPassword());
                            JuneToolsApp.getDbManager().saveOrUpdate(userEntry);
                            finish();
                        }
                    } catch (DbException e) {
                        Log.e("sherry","数据库操作失败");
                    }

                }
            }
        });
    }
}
