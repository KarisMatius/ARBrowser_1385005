package jp.androidgroup.nyartoolkit;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.Buffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.microedition.khronos.opengles.GL10;

import jp.androidgroup.nyartoolkit.HttpGetClient.AsyncTaskCallback;
import jp.androidgroup.nyartoolkit.markersystem.NyARAndMarkerSystem;
import jp.androidgroup.nyartoolkit.markersystem.NyARAndSensor;
import jp.androidgroup.nyartoolkit.sketch.AndSketch;
import jp.androidgroup.nyartoolkit.utils.camera.CameraPreview;
import jp.androidgroup.nyartoolkit.utils.gl.AndGLBox;
import jp.androidgroup.nyartoolkit.utils.gl.AndGLDebugDump;
import jp.androidgroup.nyartoolkit.utils.gl.AndGLFpsLabel;
import jp.androidgroup.nyartoolkit.utils.gl.AndGLTextLabel;
import jp.androidgroup.nyartoolkit.utils.gl.AndGLView;
import jp.nyatla.nyartoolkit.core.NyARException;
import jp.nyatla.nyartoolkit.markersystem.NyARMarkerSystemConfig;

import org.takanolab.ar.log.LogHttpClientPost;
import org.takanolab.ar.log.LogWriter;
import org.takanolab.kGLModel.KGLException;
import org.takanolab.kGLModel.KGLModelData;

import com.paar.ch9.MainActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * NyARToolkit4.0.3
 *
 */
public class NyARToolkitAndroidActivity extends AndSketch implements AndGLView.IGLFunctionEvent
{
	// Log認識用タグ
	private static String TAG = "EcoAR";
	// ActivityResultの認識コード
	private static final int FIXATION_MODEL= 1;
	// Resultキー
	public static final String RESULT_SELECT_ITEM_ID = "selectedItemId";

	CameraPreview _camera_preview;
	AndGLView _glv;
	Camera.Size _cap_size;
	// GLの描写部分のBitmap
	Bitmap GLBitmap;

	// マーカー&モデルの数
	private static final int PAT_MAX = 10;
	// 使用するモデルのパス
	private String modelPath = Environment.getExternalStorageDirectory().getPath() + "/3DModelData/";
	// ユーザが選択したモデルidを受け取る変数
//	private int markerModelId = 0;

	// 画面サイズ
	int screen_w,screen_h;
	
	// ------------- Model ---------------------------
	// modelの操作フラグ
	int manipulationMode = 0;
	int markerModelId = 0;
	// Modelの制御
	float lastX = 0;
	float lastY = 0;
	float scale = 2f;
	float xpos=0,ypos=0,zpos=0,xrot=0,yrot=0,zrot=0;
	// 固定表示をするときに使う姿勢制御の変数
	float[] center = new float[]{
			0.99548185f,
			0.091270804f,
			0.026182842f,
			0.0f,
			-0.04013392f,
			0.65435773f,
			-0.7551194f,
			0.0f,
			-0.0860533f,
			0.7506568f,
			0.6550643f,
			0.0f,
			22.488108f,
			-72.82579f,
			-359.72952f,
			1.0f
	};
	// ----------------------------------------------

	
	// ------------------ Flag --------------------
	// モデルの固定表示フラグ
	boolean displayflag = false;
	// スクリーンキャプチャフラグ
	boolean screenCapture = false;
	// Bitmapの存在フラグ
	boolean isGLBitmap = false;
	// 固定表示を行うかのフラグ
	boolean freemodeflag = false;
	// Log用フラグ
	boolean sdLogflag = true;
	// -------------------------------------------
	
	
	// ------------------- Log -------------------
	// 操作カウント
	int count_Position = 0;
	int count_Rotate = 0;
	int count_Scale = 0;
	int count_ScreenCapture = 0;
	int uiMode = 0;
	//HTTP通信で送信する操作履歴用
	String log = null;
	//--------------------------------------------
	
	
	//--------------------------------------------
	//推薦ListView
	//アイテムの親リスト
	List<Map<String, String>> parentrecommendList = new ArrayList<Map<String, String>>();
	// 各アイテムのサブアイテムのリスト
	List<List<Map<String, String>>> allsubrecommendList = new ArrayList<List<Map<String, String>>>();
	//親リストの項目要素
	Map<String, String> recommendItem;
	
	Map<String, String> subrecommendItem;
	
	ExpandableListView recommendListView;
	
	SimpleExpandableListAdapter adapter;
	
    ExpandableListView lv;
	
	SetRecommendListView rlv = new SetRecommendListView("ranking");
	String[] recommendModelnames;
	//--------------------------------------------
	
	
	// for model renderer
	private static final int CROP_MSG = 1;
	private static final int FIRST_TIME_INIT = 2;
	private static final int RESTART_PREVIEW = 3;
	private static final int CLEAR_SCREEN_DELAY = 4;
	private static final int SET_CAMERA_PARAMETERS_WHEN_IDLE = 5;
	public static final int SHOW_LOADING = 6;
	public static final int HIDE_LOADING = 7;

	// YUV420 convert RGB(naitive)
	static {
		System.loadLibrary("yuv420sp2rgb");
	}
	public static native void decodeYUV420SP(int[] rgb, byte[] yuv420sp, int width, int height, int type);
	public static native void decodeYUV420SP(byte[] rgb, byte[] yuv420sp, int width, int heught, int type);

	/**
	 * onStartでは、Viewのセットアップをしてください。
	 */
	@Override
	public void onStart(){
		super.onStart();

		long start = System.currentTimeMillis();

		FrameLayout fr=((FrameLayout)this.findViewById(R.id.sketchLayout));
		// エラー回避用
		_evlistener.clear();
		// エラー回避用
		fr.removeAllViews();
		//カメラの取得
		Log.e("OnStart", "new CameraPreview");
		this._camera_preview=new CameraPreview(this);
		// カメラの解像度
		this._cap_size=this._camera_preview.getRecommendPreviewSize(320,240);
		//		this._cap_size=this._camera_preview.getRecommendPreviewSize(640,480);
		//		this._cap_size=this._camera_preview.getRecommendPreviewSize(1280,720);
		// 画面サイズの計算（画面に表示される大きさ）
		//		int h = this.getWindowManager().getDefaultDisplay().getHeight();
		//		screen_w=(this._cap_size.width*h/this._cap_size.height);
		//		screen_h=h;
		screen_w = this.getWindowManager().getDefaultDisplay().getWidth();
		screen_h = this.getWindowManager().getDefaultDisplay().getHeight();
		//camera
		fr.addView(this._camera_preview, 0, new LayoutParams(screen_w,screen_h));
		//GLview
		this._glv=new AndGLView(this);
		fr.addView(this._glv, 0,new LayoutParams(screen_w,screen_h));
		long end = System.currentTimeMillis();
		Log.d("screen", "screensize : " + screen_w + " , " + screen_h);
		
		//
		// 右側に情報推薦用View(ListLayout)を表示します（未実装）
		//
		
		//親子リスト初期化
		parentrecommendList.clear();
		allsubrecommendList.clear();
		
		//初期推薦コンテンツ
		recommendModelnames = new String[]{"Papilio Maackii", "bison", "elk", "bighorn_sheep", "moose"};
 
        // 親ノードに表示する内容を生成
        for (int i = 0; i < 5; i++) {
            Map<String, String> parentData = new HashMap<String, String>();
            parentData.put("MODELNAME", recommendModelnames[i]);
            // 親ノードのリストに内容を格納
            parentrecommendList.add(parentData);
        }
 
        // 子ノードに表示する文字を生成
        for (int i = 0; i < 5; i++) {
            // 子ノード全体用のリスト
            List<Map<String, String>> childList = new ArrayList<Map<String, String>>();
 
            // 各子ノード用データ格納
            for (int j = 0; j < 2; j++) {
                Map<String, String> childData = new HashMap<String, String>();
                switch(j){
                case 0:
                	childData.put("MODE", "3DCG");
                	break;
                case 1:
                	childData.put("MODE", "Report");
                	break;
                }
                // 子ノードのリストに文字を格納
                childList.add(childData);
            }
            // 全体の子ノードリストに各小ノードリストのデータを格納
            allsubrecommendList.add(childList);
        }
 
        // アダプタの作成
        adapter = new SimpleExpandableListAdapter(
                this, parentrecommendList,
                android.R.layout.simple_expandable_list_item_1,
                new String[] { "MODELNAME" }, new int[] { android.R.id.text1 },
                allsubrecommendList,
                android.R.layout.simple_expandable_list_item_2,
                new String[] { "MODE" }, new int[] {
                        android.R.id.text1});
        
        recommendListView = (ExpandableListView) findViewById(R.id.expandableListView);
        
        //生成した情報をセット
        recommendListView.setAdapter(adapter);
        
        //子リストのクリックリスナー
        recommendListView.setOnChildClickListener(new OnChildClickListener() {  
    		@Override  
    		public boolean onChildClick(ExpandableListView parent, View v,  
    				int groupPosition, int childPosition, long id) {  

    			//アダプターから情報を取得
    			ExpandableListAdapter adapter = parent.getExpandableListAdapter();
    			//親リストの情報を取得
    			Map<String, Object> groupobj = (Map<String, Object>)adapter.getGroup(groupPosition);
    			//子リストの情報を取得
    			Map<String, Object>childMap = (Map<String, Object>)adapter.getChild(
    				groupPosition,
    				childPosition
    			);
                //リストの情報を格納
    			String groupitem = groupobj.get("MODELNAME").toString();
    			String childitem = childMap.get("MODE").toString();
    			
    			// クリックされたアイテムをLogに表示  
    	    	Log.i(TAG, "Child Item Click " + "Group= " + groupitem + " C= " + childitem);
    	    	
    	    	if(childitem == "3DCG"){
    	    		
    	    		markerModelId = 1;
    	    		// 固定表示フラグを立てる
    				freemodeflag = true;
    				// モードが切り替わった
    				uiMode = 1;
    	    	}
    	    	
    	    	return false;  
    		}        
    	});  
        
		//
		// ここまで
		//
			
		Log.d(TAG,"onStart Time " + (end - start) + "ms");
	}

	NyARAndSensor _ss;
	NyARAndMarkerSystem _ms;
	private int[] _mid = new int[PAT_MAX];
	// モデルの名前配列
	private String[] modelNames = new String[PAT_MAX];
	// モデルデータ
	private KGLModelData[] model_data = new KGLModelData[PAT_MAX];
	AndGLTextLabel text;
	AndGLBox box;
	AndGLFpsLabel fps;

	public void setupGL(GL10 gl)
	{
		TAG = "setupGL";
		long start = System.currentTimeMillis();
		try
		{
			Log.d(TAG,"GL Setup Start.");
			AssetManager assetMng = getResources().getAssets();
			//create sensor controller.
			this._ss=new NyARAndSensor(this._camera_preview,this._cap_size.width,this._cap_size.height,30);
			//create marker system
			this._ms=new NyARAndMarkerSystem(new NyARMarkerSystemConfig(this._cap_size.width,this._cap_size.height));

//			this._mid[0]=this._ms.addARMarker(assetMng.open("AR/data/hiro.pat"),16,25,80);
//			this._mid[1]=this._ms.addARMarker(assetMng.open("AR/data/kanji.pat"),16,25,80);
			
			for(int i =0;i<PAT_MAX;i++){
				if(i<=9){
					this._mid[i] = this._ms.addARMarker(assetMng.open("AR/data/patt0" + i + ".pat"),16,25,80);
				}else{
					this._mid[i] = this._ms.addARMarker(assetMng.open("AR/data/patt" + i + ".pat"),16,25,80);
				}
			}

			// モデルの登録
			setModelName();

			for(int i=0; i<model_data.length; i++){
				if(model_data[i] == null) {
				}else{
					model_data[i].reloadTexture(gl);
					Log.d(TAG,"reloadTexture : " + modelNames[i]);
				}
			}
			
			if(sdLogflag){
				//現在のモード
				switch(uiMode){
				case 0: 
					LogWriter.sdput("Start3DCGMode");
					log = LogWriter.get("Start3DCGMode");
					LogHttpClientPost lhcp = new LogHttpClientPost();
					lhcp.execute(log);
					break;
				case 1:
					LogWriter.sdput("StartFreeMode");
					log = LogWriter.get("StartFreeMode");
					LogHttpClientPost lhcp2 = new LogHttpClientPost();
					lhcp2.execute(log);
					break;
				}
			}
			
			this._ss.start();
			//setup openGL Camera Frustum
			gl.glMatrixMode(GL10.GL_PROJECTION);
			gl.glLoadMatrixf(this._ms.getGlProjectionMatrix(),0);
			this.text=new AndGLTextLabel(this._glv);
			this.box=new AndGLBox(this._glv,40);
			this._debug=new AndGLDebugDump(this._glv);
			this.fps=new AndGLFpsLabel(this._glv,"MarkerPlaneActivity");
			this.fps.prefix=this._cap_size.width+"x"+this._cap_size.height+":";

			long end = System.currentTimeMillis();
			Log.d(TAG,"GL Setup End.");
			Log.d(TAG,"setupGL Time " + (end - start) + "ms");

		} catch (Exception e) {
			e.printStackTrace();
			this.finish();
		}
	}
	
	//モデルの登録を行う
	private void setModelName(){
				//Animal Model
		
		modelNames[0] = "brilliant_blue_discus_fish";
		modelNames[1] = "brown_trout";
		modelNames[2] = "grayling";
		modelNames[3] = "mountain_white_fish";
		modelNames[4] = "western_longnose_sucker";
		modelNames[5] = "rainbow_trout";
		modelNames[6] = "grizzly_bear";
		modelNames[7] = "hoary_marmot";
		modelNames[8] = "canada_lynx";
		modelNames[9] = "moose";
		
//				modelNames[0] = "bald_eagle";
//				modelNames[1] = "bison";
//				modelNames[2] = "bighorn_sheep";
//				modelNames[3] = "cougar";
//				modelNames[4] = "coyote";
//				modelNames[5] = "elk";
//				modelNames[6] = "grizzly_bear";
//				modelNames[7] = "hoary_marmot";
//				modelNames[8] = "canada_lynx";
//				modelNames[9] = "moose";
//				modelNames[10] = "mountain_goat";
//				modelNames[11] = "desert_tortoise";
//				modelNames[12] = "gray_wolf";
//				modelNames[13] = "rattlesnake";
//				//Bird Model
//				modelNames[14] = "bald_eagle";
//				modelNames[15] = "baltimore_oriole";
//				modelNames[16] = "black_backed_woodpecker";
//				modelNames[17] = "broad_winged_hawk";
//				modelNames[18] = "great_horned_owl";
//				modelNames[19] = "grus_americana";
//				modelNames[20] = "spruce_grouse";
//				modelNames[21] = "steller_s_jay";
//				modelNames[22] = "virginia_rail";
//				//Fish Model
//				modelNames[23] = "brilliant_blue_discus_fish";
//				modelNames[24] = "brown_trout";
//				modelNames[25] = "grayling";
//				modelNames[26] = "mountain_white_fish";
//				modelNames[27] = "western_longnose_sucker";
//				//Insect Model
//				modelNames[28] = "ephemeroptera";
//				modelNames[29] = "ladybug";
//				modelNames[30] = "leptinotarsa_decemlineata";
//				modelNames[31] = "melanoplus_spretus";
//				modelNames[32] = "sympetrum_danae";
//				//Plant Model
//				modelNames[33] = "campanula_rotundifolia";
//				modelNames[34] = "machingun_lily";
//				modelNames[35] = "pinus_banksiana";
	}

	AndGLDebugDump _debug=null;

	/**
	 * 継承したクラスで表示したいものを実装してください
	 * @param gl
	 */
	public void drawGL(GL10 gl)
	{
		try{
			//背景塗り潰し色の指定
			gl.glClearColor(0,0,0,0);
			//背景塗り潰し
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT|GL10.GL_DEPTH_BUFFER_BIT);
			if(ex!=null){
				_debug.draw(ex);
				return;
			}
			fps.draw(0, 0);
			if(!freemodeflag){
				synchronized(this._ss){
					this._ms.update(this._ss);
					for(int id : _mid){
						if(this._ms.isExistMarker(id)) drawModelData(gl, id);
					}
				}
			}
			if(freemodeflag){
				// フラグが立っているときモデルを固定表示
				drawModelDataFixation(gl, markerModelId);
			}
			if(screenCapture){
				// フラグが立ってるときGL描写部分を画像で保存
				glScreenCapture(gl);
			}
		}catch(Exception e)
		{
			ex=e;
		}
	}


	private KGLModelData getCreateModel(GL10 gl, int id){
		TAG = "getCreateModel";
		Log.d(TAG,"Create Now!");
		try {
			return KGLModelData.createGLModel(gl, null, modelPath, modelNames[id] + ".mqo", 0.02f);
		} catch (KGLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * idに一致するモデルを描写します
	 * 
	 * @param gl
	 * @param id
	 * @throws NyARException
	 */
	private void drawModelData(GL10 gl,int id) throws NyARException{
		
		//現在参照しているmodelのnameをサーバに送信
		RecommendHttpClient rhc = new RecommendHttpClient();
		rhc.execute(modelNames[markerModelId]);
		
		if(model_data[id] == null){
			Log.d(TAG,modelNames[id] + " is NULL Model Create!");
			model_data[id] = getCreateModel(gl, id);
			
			//IDに一致したマーカー上にモデルが描写されたことをLogに出力
			if(sdLogflag){
				LogWriter.sdput("DrawModel," + modelNames[id]);
				log = LogWriter.get("DrawModel," + modelNames[id]);
				LogHttpClientPost lhcp = new LogHttpClientPost();
				lhcp.execute(log);
			}
//			return;
		}else{
//			Log.d(TAG,modelNames[id] + "is Not Null Texture Reload");
//			model_data[id].reloadTexture(gl);
		}
		
		this.text.draw("found" + this._ms.getConfidence(id),0,16);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadMatrixf(this._ms.getGlMarkerMatrix(id),0);

		gl.glTranslatef(this.xpos, this.ypos, this.zpos);
		// OpenGL座標系→ARToolkit座標系
		gl.glRotatef(this.xrot, 1.0f,0.0f,0.0f);
		gl.glRotatef(this.yrot, 0.0f,1.0f,0.0f);
		gl.glRotatef(this.zrot, 0.0f,0.0f,1.0f);
		gl.glScalef(this.scale, this.scale, this.scale);
		model_data[id].enables(gl, 10.0f);
		model_data[id].draw(gl);
		model_data[id].disables(gl);
		
		markerModelId = id;
//		System.out.println("drawmodel2 マーカーid : " + markerModelId);
	}


	/**
	 * nameの一致するモデルを描写します<br>
	 * マーカーに左右されず画面上に表示されます．
	 * 
	 * @param gl
	 * @param name
	 * @throws NyARException
	 */
	private void drawModelDataFixation(GL10 gl,int id) throws NyARException{
		TAG = "drawModelDataFixation";
		
		//System.out.println("drawmodelfixation1 マーカーid : " + id);
		//現在参照しているmodelのnameをサーバに送信
		RecommendHttpClient rhc = new RecommendHttpClient();
		rhc.execute(modelNames[markerModelId]);
		
		if(model_data[id] == null){
			Log.d(TAG,modelNames[id] + " is NULL Model Create!");
			model_data[id] = getCreateModel(gl, id);
			
			//IDに一致したマーカー上にモデルが描写されたことをSdLogに出力
			if(sdLogflag){
				LogWriter.sdput("selectFixationModel," + modelNames[markerModelId]);
				log = LogWriter.get("selectFixationModel," + modelNames[markerModelId]);
				LogHttpClientPost lhcp = new LogHttpClientPost();
				lhcp.execute(log);
			}
		
//			return;
		}else{
//			Log.d(TAG,modelNames[id] + "is Not Null Texture Reload");
//			model_data[id].reloadTexture(gl);
		}
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadMatrixf(center,0);

		// 固定表示のための姿勢位置を取得するためのLog
		//		gl.glLoadMatrixf(this._ms.getGlMarkerMatrix(id),0);
		//		String str = "";
		//		float[] mat = this._ms.getGlMarkerMatrix(id);
		//		for(float fl : mat){
		//			str += fl + "\n";
		//		}
		//		Log.d(TAG,"Matrix\n" + str + "end.");

		gl.glTranslatef(this.xpos, this.ypos, this.zpos);
		// OpenGL座標系→ARToolkit座標系
		gl.glRotatef(this.xrot, 1.0f,0.0f,0.0f);
		gl.glRotatef(this.yrot, 0.0f,1.0f,0.0f);
		gl.glRotatef(this.zrot, 0.0f,0.0f,1.0f);
		gl.glScalef(this.scale, this.scale, this.scale);
		model_data[id].enables(gl, 10.0f);
		model_data[id].draw(gl);
		model_data[id].disables(gl);
		
		markerModelId = id;
//		System.out.println("drawmodelfixation2 マーカーid : " + markerModelId);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		// メニューアイテムの追加
		menu.add(Menu.NONE, 0, Menu.NONE, "Position");
		menu.add(Menu.NONE, 1, Menu.NONE, "Rotate");
		menu.add(Menu.NONE, 2, Menu.NONE, "Scale");
		menu.add(Menu.NONE, 3, Menu.NONE, "ScreenCapture");
		menu.add(Menu.NONE, 4, Menu.NONE, "Recommend");
		menu.add(Menu.NONE, 5, Menu.NONE, "FreeMode");
		menu.add(Menu.NONE, 6, Menu.NONE, "QuestMode");
		menu.add(Menu.NONE, 7, Menu.NONE, "SearchMode");		
		menu.add(Menu.NONE, 8, Menu.NONE, "Exit");

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if(freemodeflag){
			menu.findItem(5).setTitle("Clear");
		}else{
			menu.findItem(5).setTitle("FreeMode");
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// addしたときのIDで識別
		switch (item.getItemId()) {
		case 0:
			manipulationMode = 0;
			count_Position++;
			if(sdLogflag){
				LogWriter.sdput("Position," + modelNames[markerModelId]);
				log = LogWriter.get("Position," + modelNames[markerModelId]);
				LogHttpClientPost lhcp = new LogHttpClientPost();
				lhcp.execute(log);
			}
			return true;

		case 1:
			manipulationMode = 1;
			count_Rotate++;
			if(sdLogflag){
				LogWriter.sdput("Rotate," + modelNames[markerModelId]);
				log = LogWriter.get("Rotate," + modelNames[markerModelId]);
				LogHttpClientPost lhcp = new LogHttpClientPost();
				lhcp.execute(log);
			}
			return true;

		case 2:
			manipulationMode = 2;
			count_Scale++;
			if(sdLogflag){
				LogWriter.sdput("Scale," + modelNames[markerModelId]);
				log = LogWriter.get("Scale," + modelNames[markerModelId]);
				LogHttpClientPost lhcp = new LogHttpClientPost();
				lhcp.execute(log);
			}
			return true;
			
		case 3:
			Shot();
			count_ScreenCapture++;
			if(sdLogflag){
				LogWriter.sdput("ScreenCapture," + modelNames[markerModelId]);
				log = LogWriter.get("ScreenCapture," + modelNames[markerModelId]);
				LogHttpClientPost lhcp = new LogHttpClientPost();
				lhcp.execute(log);
			}
			return true;
			
		case 4:
			//
			//RecommendListViewの更新
			//
			
			//これまで表示していたコンテンツをクリア
			parentrecommendList.clear();
			allsubrecommendList.clear();
			recommendListView.invalidateViews();
			
			//サーバから関連コンテンツを取得
			HttpGetClient hgc = new HttpGetClient(new AsyncTaskCallback() {
		        public void preExecute() {
		            //だいたいの場合ダイアログの表示などを行う
		        }
		        public void postExecute(String result) {
		            //AsyncTaskの結果を受け取り「，」で分割し配列に格納
		        	recommendModelnames = result.split(",", 0);
			
			        // 親ノードに表示する内容を生成
			        for (int i = 0; i < 5; i++) {
			            Map<String, String> parentData = new HashMap<String, String>();
			            parentData.put("MODELNAME", recommendModelnames[i]);
			            // 親ノードのリストに内容を格納
			            parentrecommendList.add(parentData);
			        }
			 
			        // 子ノードに表示する文字を生成
			        for (int i = 0; i < 5; i++) {
			            // 子ノード全体用のリスト
			            List<Map<String, String>> childList = new ArrayList<Map<String, String>>();
			 
			            // 各子ノード用データ格納
			            for (int j = 0; j < 2; j++) {
			                Map<String, String> childData = new HashMap<String, String>();
			                switch(j){
			                case 0:
			                	childData.put("MODE", "3DCG");
			                	break;
			                case 1:
			                	childData.put("MODE", "Report");
			                	break;
			                }
			                // 子ノードのリストに文字を格納
			                childList.add(childData);
			            }
			            // 全体の子ノードリストに各小ノードリストのデータを格納
			            allsubrecommendList.add(childList);
			        }
		        	//ListViewを更新
					adapter.notifyDataSetChanged();
		        }
			});
			hgc.execute("ranking");
			return true;
			
		case 5:
			if(freemodeflag){
				freemodeflag = false;
				// モードが変わった
				uiMode = 0;
				if(sdLogflag){
					LogWriter.sdput("Start3DCGMode");
					log = LogWriter.get("Start3DCGMode");
					LogHttpClientPost lhcp = new LogHttpClientPost();
					lhcp.execute(log);
				}
			}else{
				selectFixationModel();
			}
			return true;
			
		case 6:
			Intent questintent = new Intent(jp.androidgroup.nyartoolkit.NyARToolkitAndroidActivity.this,com.paar.ch9.MainActivity.class);
			startActivity(questintent);
			//カメラの停止
			try {
				_camera_preview.onAcStop();
			} catch (Exception e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			LogWriter.sdput("StartQuestMode");
			log = LogWriter.get("StartQuestMode");
			LogHttpClientPost lhcp = new LogHttpClientPost();
			lhcp.execute(log);
			return true;
		
		case 7:
			// change to search activity
            Intent searchintent = new Intent(jp.androidgroup.nyartoolkit.NyARToolkitAndroidActivity.this, org.takanolab.ar.search.SearchActivity.class);
            startActivity(searchintent);
            LogWriter.sdput("StartSearchMode");
            log = LogWriter.get("StartSearchMode");
			LogHttpClientPost lhcp2 = new LogHttpClientPost();
			lhcp2.execute(log);
            return true;
			
		case 8:
			finish();
			if(sdLogflag){
				LogWriter.sdput("Finish");
				log = LogWriter.get("Finish");
				LogHttpClientPost lhcp3 = new LogHttpClientPost();
				lhcp3.execute(log);
			}
			break;
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		Log.d("OnTouch","ontouch");
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			lastX = event.getX();
			lastY = event.getY();
			break;

		case MotionEvent.ACTION_MOVE:
			float dX = lastX - event.getX();
			float dY = lastY - event.getY();
			lastX = event.getX();
			lastY = event.getY();

			switch(manipulationMode){
			case 0 :
				setXpos(-dX/1.0f);
				setYpos(dY/1.0f);
				Log.d("ontatuc",xpos +"/"+  ypos);
				break;
			case 1 :
				setXrot(0.80f*-dY);
				setYrot(0.80f*-dX);
				Log.d("rotate",xrot +"/"+yrot);
				return true;
			case 2 :
				setScale(dY/10.0f);
				Log.d("scale",scale + "");
				return true;
			}

		case MotionEvent.ACTION_UP:
			break;
		}
		return true;
	}

	public void setScale(float f) {
		this.scale += f;
		if(this.scale < 0.0001f)
			this.scale = 0.0001f;
	}

	public void setXrot(float dY) {
		this.xrot += dY;
	}

	public void setYrot(float dX) {
		this.yrot += dX;
	}

	public void setXpos(float f) {
		this.xpos += f;
	}

	public void setYpos(float f) {
		this.ypos += f;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		TAG = "onActivityResult";
		// インテントのリザルトを受け取る
		if(requestCode == FIXATION_MODEL){
			if(resultCode == RESULT_OK){
				// 固定表示フラグを立てる
				freemodeflag = true;
				// モードが切り替わった
				uiMode = 1;
				
				// 表示するモデルidをセット
				markerModelId = data.getIntExtra(RESULT_SELECT_ITEM_ID, 0);
//				if(sdLogflag) SdLog.put("selectFixationModel," + modelNames[markerModelId]);
				Log.d(TAG,"getItemId " + data.getIntExtra(RESULT_SELECT_ITEM_ID, 0));
			}
		}
	}

	/**
	 *  固定表示させるモデルを選択する（アクティビティー切り替え）
	 *  
	 */
	private void selectFixationModel(){
		// インテント作成
		Intent it = new Intent();
		// モデル名配列をセット
		it.putExtra("FixationModel", modelNames);
		// 遷移先をセット
		it.setClassName("jp.androidgroup.nyartoolkit","jp.androidgroup.nyartoolkit.FixationModelActivity");
		// リザルト付きアクティビティースタート
		startActivityForResult(it, FIXATION_MODEL);
	}


	/**
	 * スクリーンキャプチャを撮る．<br>
	 * takePictureでうまくいかなかったので、プレビューを加工してBitmpで保存する．<br>
	 * CGModelは写らないので{@link #glScreenCapture(GL10)}を使う．<br>
	 * 参考 : http://android.roof-balcony.com/camera/preview-get/
	 * 
	 * @author s0921122
	 * @version 1.0
	 */
	private void Shot(){
		TAG = "Shot";
		Log.d(TAG,"Screen Capture Start");
		int width = _cap_size.width;
		int height = _cap_size.height;
		Bitmap cameraBitmap = null;

		try{
			Log.d(TAG,"Empty BMP(ARG8888) Create.");
			// ARGB8888の画素の配列
			int[] rgb = new int[(width * height)]; 
			// ARGB8888で空のビットマップ作成
			cameraBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888); 

			Log.d(TAG,"Decode Start.");
			// YUV420からRGBに変換
			decodeYUV420SP(rgb, _camera_preview.getCurrentBuffer(), width, height, 2);
			Log.d(TAG,"Decode done.");
			Log.d(TAG,"Camera Preview Capture Create Start.");
			// 変換した画素からビットマップにセット
			cameraBitmap.setPixels(rgb, 0, width, 0, 0, width, height); 
			Log.d(TAG,"Camera Preview Capture Create done.");

			// GL描写部分のスクリーンキャプチャを撮るフラグを立てる
			screenCapture = true;

			// GL描写部分のスクリーンキャプチャができるまで待機
			while(true){
				if(isGLBitmap) break;
			}
			// 画像を重ね合わせて保存する
			overlayBMP(cameraBitmap, GLBitmap);

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * GLで描写している部分をキャプチャする<br>
	 * 参考 : http://www.anddev.org/how_to_get_opengl_screenshot__useful_programing_hint-t829.html
	 * 
	 * @author s0921122
	 * @version 1.0
	 * @param gl
	 */
	private void glScreenCapture(GL10 gl){
		TAG = "glScreenCapture";
		Log.d(TAG,"GL Drawing Screen Capture Start.");
		// キャプチャフラグを下げる
		screenCapture = false;
		// スクリーンキャプチャする範囲
		int takeWidth = screen_w;
		int takeHeight = screen_h;
		int[] tmp = new int[takeHeight*takeWidth];
		int[] screenshot = new int[takeHeight*takeWidth];
		Buffer screenshotBuffer = IntBuffer.wrap(tmp);
		screenshotBuffer.position(0);
		gl.glReadPixels(0,0,takeWidth,takeHeight, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, screenshotBuffer); 

		for(int i=0; i<takeHeight; i++) 
		{
			//remember, that OpenGL bitmap is incompatible with Android bitmap 
			//and so, some correction need.      
			for(int j=0; j<takeWidth; j++) 
			{ 
				int pix=tmp[i*takeWidth+j]; 
				int pb=(pix>>16)&0xff; 
				int pr=(pix<<16)&0x00ff0000; 
				int pix1=(pix&0xff00ff00) | pr | pb; 
				screenshot[(takeHeight-i-1)*takeWidth+j]=pix1; 
			} 
		} 
		// アルファありのBitmapを作成する
		// この時にリサイズで保存はできない（変な画像になる）
		Bitmap temp = Bitmap.createBitmap(screenshot, takeWidth, takeHeight, Config.ARGB_8888);
		//　作成したBitmapを元にカメラプレビューサイズにリサイズしたBitmapを作成
		this.GLBitmap = Bitmap.createScaledBitmap(temp, _cap_size.width, _cap_size.height, true);
		temp.recycle();

		Log.d(TAG,"GL Drawing Screen Capture. Create done.");
		// GLをキャプチャしたBitmapを作成したのでフラグを立てる
		isGLBitmap = true;
	}


	/**
	 * 2枚のBitmap重ね合わせて保存する．<br>
	 * このプログラムではカメラとGLのBitmapを重ね合わせて保存する．
	 * 
	 * @param under 背景になる画像
	 * @param upper 上に乗る画像
	 */
	private void overlayBMP(Bitmap under,Bitmap upper){
		TAG = "overlayBMP";
		Log.d(TAG,"Bitmap Ovelray Start.");
		//ARGB_8888,RGB_565,ARGB_4444
		int width = under.getWidth();
		int height = under.getHeight();

		Log.d(TAG,"Create Base Bitmap.");
		// 合成するための下地
		Bitmap newBitmap = Bitmap.createBitmap(width, height,Bitmap.Config.RGB_565);

		Log.d(TAG,"Create Canvas.");
		Canvas canvas = new Canvas(newBitmap);
		Log.d(TAG,"Overlay Start.");
		canvas.drawBitmap(under, 0, 0, (Paint)null);
		canvas.drawBitmap(upper, 0, 0, (Paint)null);
		Log.d(TAG,"Overlay done.");
		Log.d(TAG,"Recycle Start.");
		upper.recycle();
		under.recycle();
		GLBitmap.recycle();
		isGLBitmap = false;
		Log.d(TAG,"Recycle done.");

		FileOutputStream fos;
		try{
			Log.d(TAG,"FileOutput Start.");
			String path = new StringBuilder()
			.append(Environment.getExternalStorageDirectory().getPath()).append("/")
			//.append("Pictures/Screenshots/")
			.append("SampleMQO").append(".jpg")
			.toString();

			fos = new FileOutputStream(path);
			// JPEGで保存
			newBitmap.compress(CompressFormat.JPEG, 100, fos);
			Toast.makeText(this, "ScreenCapture Success.", Toast.LENGTH_LONG).show();
			Log.d(TAG,"FileOutput end. \nOutput Path : " + path);
			Log.d(TAG,"ScreenCapture end.");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	Exception ex=null;
	
	private static HashMap<String, Integer> modelidmap = new HashMap<String, Integer>() {{
		put("canada_goose", 1);
		put("tundra_swan", 2);
		put("snow_goose", 3);
		put("ross_s_goose", 4);
		put("wood_duck", 5);
		put("gadwall", 6);
		put("eurasian_wigeon", 7);
		put("american_wigeon", 8);
		put("mallard", 9);
		put("blue_winged_teal", 10);
		put("cinnamon_teal", 11);
		put("northern_shoveler", 12);
		put("northern_pintail", 13);
		put("green_winged_teal", 14);
		put("canvasback", 15);
		put("redhead", 16);
		put("ring_necked_duck", 17);
		put("greater_scaup", 18);
		put("lesser_scaup", 19);
		put("harlequin_duck", 20);
		put("irginia_rail", 21);
		put("broad_winged_hawk", 22);
		put("great_horned_owl", 23);
		put("grus_americana", 24);
		put("baltimore_oriole", 25);
		put("", 26);
	}};
}
