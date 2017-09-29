# BaseMVP Android MVP基本框架，依赖retrofit2，okhttp3，rx1

## 使用

```
compile 'ren.yale.android:basemvplib:0.5'
```

### Appclication 继承

```
public class App extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();
    }
}

```

### Retrofit Api

- 初始化，在Application

```
ApiManager.getInstance().init(this);

```

- 定义Retrofit 接口

```
public interface TestApi {
    @GET("test")
    Observable<Test> test();
}
```
- 获取 Retrofit 对象

```
private static TestApi testApi;

public TestApi getTestApi(){
     if (testApi==null){
         testApi = HttpRequestManger.getInstance().configRetrofit(TestApi.class,URL);
     }
     return testApi;
 }

```

- 完整代码

```
public class ApiManager {

    private static TestApi testApi;
    private static final String URL ="https://mock.daoxuehao.com/mock/59cc6c3991895710a06deeef/test/";

    private static class Holder{
        public static volatile  ApiManager apiManager = new ApiManager();
    }

    public static ApiManager getInstance(){
        return Holder.apiManager;
    }

    public void init(Context context){
        HttpRequestManger.getInstance().setIHttpHeader(new IHttpHeader() {
            @Override
            public Map getHttpHeader(String url, Map mapParams) {
                return null;
            }
        }).setInterceptor(null).setNetWorkInterceptor(null).init(context);
    }

    public TestApi getTestApi(){
        if (testApi==null){
            testApi = HttpRequestManger.getInstance().configRetrofit(TestApi.class,URL);
        }
        return testApi;
    }
}

```

### MVP

- Contract

```
public interface TestContract {

    interface View extends BaseView{

        void getTestSuccess(Test test);
    }
    interface  Model extends BaseModel{
        BaseObservable<Test> getTest();

    }
    abstract  class TestPresenter extends BasePresenter<Model,View>{
       abstract void getTest();
    }
}

```

- Model

```
public class TestModel implements TestContract.Model {
    @Override
    public BaseObservable<Test> getTest() {
        return BaseObservable.create(ApiManager.getInstance().getTestApi().test())
                .cacheIoMain();
    }
}
```

- Presenter

```
public class TestPresenter extends TestContract.TestPresenter {

    public void getTest(){
        mModel.getTest().bindDestoryEvent(getPublishSubject())
                .subscribe(new ProgressSubscriber<Test>(getProgressDlg()){
                    @Override
                    public void onNext(Test test) {
                        mView.getTestSuccess(test);
                    }
                });
    }

}
```

- Activity继承

```
public class MainActivity extends MVPActivity<TestPresenter,TestModel>
 implements TestContract.View{

    private static final String  TAG="test";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Dialog getProgressDlg() {
        return new Dialog(this);
    }
    public void onClick(View v){
        mPresenter.getTest();
    }

    @Override
    public void getTestSuccess(Test test) {
        Log.d(TAG,test.getMessage());
    }
}
```

## 混淆
https://github.com/yale8848/RetrofitCache 混淆