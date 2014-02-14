package com.gmail.mathck;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import com.gmail.mathck.R;

//----------------------------------------------------------------------
//									TEAM SYNERGY	-	SLIDE v1.0
//----------------------------------------------------------------------
//	DOES NOT SUPPORT
//		-	XHDPI
//----------------------------------------------------------------------
//	TESTED ON
//		-	NEXUS 7
//		-	HTC DESIRE S
//----------------------------------------------------------------------
public class MainGamePanel extends SurfaceView implements SurfaceHolder.Callback {

	private MainThread thread;

	//----------------------------------------------------------------------
	//	DISPLAY SIZE
	//----------------------------------------------------------------------
	DisplayMetrics metrics = this.getResources().getDisplayMetrics();
	private float WIDTH = metrics.widthPixels;
	private float HEIGHT = metrics.heightPixels;

	//----------------------------------------------------------------------
	//	5 GAME STATES :		|	MENU	|	GAME	|	SCORE	|	PGAME
	//						|	MED
	//----------------------------------------------------------------------
	private enum state { menu, game, score, postGame, med }
	state game_state = state.menu;
	private boolean postGame_delay = true;
	private boolean showBubble = (MainActivity.highscore >= 100) ? false : true;
	private boolean pro = !showBubble;

	//----------------------------------------------------------------------
	//	SCORE	+	FONT
	//----------------------------------------------------------------------
	private int SCORE = 0;
	private Paint scoreText = new Paint();
	private Paint highscoreText = new Paint();

	//----------------------------------------------------------------------
	//	SOUND
	//----------------------------------------------------------------------
	private SoundPool sounds;
	private int sClick;
	private int sDing;

	//----------------------------------------------------------------------
	//	TIME
	//----------------------------------------------------------------------
	private long time_start = 0;
	private long time_begin = 2500;
	private long time_elapsed = 0;
	private long time_end = time_begin;

	//----------------------------------------------------------------------
	//	BACKGROUNDS
	//----------------------------------------------------------------------
	private CBackground bg_main;
	private CBackground bg_ingame;
	private CProgressBar pb_time;

	//----------------------------------------------------------------------
	//	BUTTONS
	//----------------------------------------------------------------------
	private CButton btn_logo;
	private CButton btn_logo_pro;
	private CButton btn_logo_score;
	private CButton btn_play;
	private CButton btn_med;
	private CButton btn_score;
	private CButton btn_play_clicked;
	private CButton btn_score_clicked;
	private CButton btn_bubble;
	private CButton btn_bubble_med;
	private CButton top_bar;
	private CButton pb_bg;
	private CCheckpoint ckp[] = new CCheckpoint[9];

	//----------------------------------------------------------------------
	//	FORMS
	//----------------------------------------------------------------------
	private Random random = new Random();
	private int random_int = 0;
	private int next_random_int = 0;
	private float form_pos_x = (WIDTH / 2) - (BitmapFactory.decodeResource(getResources(), R.drawable.form_1).getWidth() / 2);
	private float form_pos_y = ((HEIGHT * 1.15f) / 2) - (BitmapFactory.decodeResource(getResources(), R.drawable.form_1).getHeight() / 2);
	private CForm form[] = new CForm[20];
	private CMiniForm miniform[] = new CMiniForm[20];
	private CLine line;

	public MainGamePanel(Context context) throws IOException {
		super(context);
		getHolder().addCallback(this);
		thread = new MainThread(getHolder(), this);

		//----------------------------------------------------------------------
		//	GET RESOURCES
		//----------------------------------------------------------------------
		line = new CLine(WIDTH / 40.0f);
		sounds = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		sClick = sounds.load(context, R.raw.click, 1);
		sDing = sounds.load(context, R.raw.ding, 2);

		//	FONT	------------------------------------------------------------
		scoreText.setColor(Color.BLACK);
		scoreText.setTextSize(WIDTH / 10);
		scoreText.setStyle(Style.FILL);
		scoreText.setTextAlign(Align.CENTER);
		scoreText.setTypeface(Typeface.createFromAsset(context.getAssets(), "DS-DIGI.TTF"));

		highscoreText.setColor(Color.BLACK);
		highscoreText.setTextSize(WIDTH / 4);
		highscoreText.setStyle(Style.FILL);
		highscoreText.setTextAlign(Align.CENTER);
		highscoreText.setTypeface(Typeface.createFromAsset(context.getAssets(), "DS-DIGI.TTF"));

		for(int i = 0; i <= 8; i++)
			ckp[i] = new CCheckpoint(BitmapFactory.decodeResource(getResources(), R.drawable.checkpoint));

		btn_logo = new CButton(BitmapFactory.decodeResource(getResources(), R.drawable.logo));
		btn_logo_pro = new CButton(BitmapFactory.decodeResource(getResources(), R.drawable.logo2));
		btn_logo_score = new CButton(BitmapFactory.decodeResource(getResources(), R.drawable.scorescreen));
		btn_play = new CButton(BitmapFactory.decodeResource(getResources(), R.drawable.play));
		btn_score = new CButton(BitmapFactory.decodeResource(getResources(), R.drawable.score));
		btn_play_clicked = new CButton(BitmapFactory.decodeResource(getResources(), R.drawable.play_click));
		btn_score_clicked = new CButton(BitmapFactory.decodeResource(getResources(), R.drawable.score_click));
		btn_bubble = new CButton(BitmapFactory.decodeResource(getResources(), R.drawable.bubble));
		btn_bubble_med = new CButton(BitmapFactory.decodeResource(getResources(), R.drawable.bubble2));
		btn_med = new CButton(BitmapFactory.decodeResource(getResources(), R.drawable.medal));

		bg_main = new CBackground(BitmapFactory.decodeResource(getResources(), R.drawable.main_bg));
		bg_ingame = new CBackground(BitmapFactory.decodeResource(getResources(), R.drawable.ingame_bg));
		top_bar = new CButton(BitmapFactory.decodeResource(getResources(), R.drawable.top_bar));
		pb_bg = new CButton(BitmapFactory.decodeResource(getResources(), R.drawable.loading_pre));
		pb_time = new CProgressBar(BitmapFactory.decodeResource(getResources(), R.drawable.loading));

		//	FORMS	------------------------------------------------------------
		try {
			for(int i = 0; i < 20; i++) {
				form[i] = new CForm(BitmapFactory.decodeResource(getResources(), CResHandler.form[i]));
				miniform[i] = new CMiniForm(BitmapFactory.decodeResource(getResources(), CResHandler.miniform[i]));
			}
		}
		catch(OutOfMemoryError error)  {
			AssetManager assetManager = context.getAssets();
			InputStream inputStream = assetManager.open("form_0.png");
			
			for(int i = 0; i < 20; i++) {
				inputStream = assetManager.open("form_" + i + ".png");

				form[i] = new CForm(BitmapFactory.decodeStream(inputStream));
				
				miniform[i] = new CMiniForm(BitmapFactory.decodeResource(getResources(), CResHandler.miniform[i]));
			}
			
			inputStream.close();
			
			form_pos_x = (WIDTH / 2) - (form[0].getWidth() / 2);
			form_pos_y = ((HEIGHT * 1.15f) / 2) - (form[0].getHeight() / 2);
		}

		setFocusable(true);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int WIDTH,
			int HEIGHT) {
	}

	public void surfaceCreated(SurfaceHolder holder) {
		if(!thread.isAlive()){
			thread.setRunning(true);
			thread.start();
		}

		//----------------------------------------------------------------------
		//	POSITION	+	SCALE
		//----------------------------------------------------------------------
		for(int i = 0; i < 20; i++) {
			form[i].scale((WIDTH / form[i].getWidth()) * 0.75f);
			form[i].setPosition(form_pos_x, form_pos_y);
			miniform[i].scale((WIDTH / miniform[0].getWidth()) * 0.12f);
			miniform[i].translate(.93f * WIDTH - miniform[0].getWidth(), HEIGHT * 0.033f);
		}

		btn_logo.scale((WIDTH / btn_logo.getWidth()) * 0.85f);
		btn_logo_pro.scale((WIDTH / btn_logo_pro.getWidth()) * 0.85f);
		btn_play.scale((WIDTH / btn_play.getWidth()) * 0.9f);
		btn_play_clicked.scale((WIDTH / btn_play_clicked.getWidth()) * 0.9f);
		btn_score.scale((WIDTH / btn_score.getWidth()) * 0.9f);
		btn_score_clicked.scale((WIDTH / btn_score_clicked.getWidth()) * 0.9f);

		btn_med.scale((WIDTH / btn_med.getWidth()) * 1.1f);
		btn_med.translate(WIDTH / 2 - btn_med.getWidth() / 2, 0);

		//----------------------------------------------------------------------
		//	MENU LAYOUT			
		//----------------------------------------------------------------------
		//						|
		//		   LOGO			| @ 11% HEIGHT
		//						|
		//		   PLAY			| @ 48% HEIGHT
		//						|
		//		   SCORE		| @ 87% HEIGHT
		//						|
		//----------------------------------------------------------------------
		btn_logo.translate(WIDTH / 2 - btn_logo.getWidth() / 2, HEIGHT * 0.11f - btn_logo.getHeight() / 2);
		btn_logo_pro.translate(WIDTH / 2 - btn_logo_pro.getWidth() / 2, HEIGHT * 0.11f - btn_logo_pro.getHeight() / 2);
		btn_play.translate(WIDTH / 2 - btn_play.getWidth() / 2, HEIGHT * 0.48f - btn_play.getHeight() / 2);
		btn_play_clicked.translate(WIDTH / 2 - btn_play.getWidth() / 2, HEIGHT * 0.48f - btn_play.getHeight() / 2);
		btn_score.translate(WIDTH / 2 - btn_score.getWidth() / 2, HEIGHT * 0.87f - btn_score.getHeight() / 2);
		btn_score_clicked.translate(WIDTH / 2 - btn_score.getWidth() / 2, HEIGHT * 0.87f - btn_score.getHeight() / 2);
		//----------------------------------------------------------------------

		btn_bubble.scale2((WIDTH / btn_bubble.getWidth()) * 1.0f);
		btn_bubble.translate(WIDTH / 2 - btn_bubble.getWidth() / 2, HEIGHT - btn_bubble.getHeightAS());

		btn_bubble_med.scale2((WIDTH / btn_bubble_med.getWidth()) * 1.0f);
		btn_bubble_med.translate(WIDTH / 2 - btn_bubble_med.getWidth() / 2, HEIGHT - btn_bubble_med.getHeightAS());

		top_bar.scale2((WIDTH / top_bar.getWidth()) * 1.0f);
		top_bar.translate(WIDTH / 2 - top_bar.getWidth() / 2, 0);

		pb_bg.translate(WIDTH * 0.3f, (HEIGHT * 0.071f) - (pb_bg.getHeight() / 2));
		pb_time.translate(WIDTH * 0.3f, (HEIGHT * 0.071f) - (pb_bg.getHeight() / 2));

		btn_logo_score.scale((WIDTH / btn_logo_score.getWidth()) * 0.9f);
		btn_logo_score.translate(WIDTH / 2 - btn_logo_score.getWidth() / 2, HEIGHT / 2 - btn_logo_score.getHeight() / 2);

		//----------------------------------------------------------------------
		//	CHECKPOINTS
		//----------------------------------------------------------------------
		for(int i = 0; i <= 8; i++)
			ckp[i].scale((WIDTH / form[i].getWidth()) * 0.75f);

		ckp[0].translate(form[0].getX(), form[0].getY());
		ckp[1].translate(ckp[0].getX() + ckp[0].getWidth(), form[0].getY());
		ckp[2].translate(ckp[1].getX() + ckp[1].getWidth(), form[0].getY());

		ckp[3].translate(form[0].getX(), ckp[2].getY() + ckp[0].getHeight());
		ckp[4].translate(ckp[0].getX() + ckp[0].getWidth(), ckp[3].getY());
		ckp[5].translate(ckp[1].getX() + ckp[1].getWidth(), ckp[3].getY());

		ckp[6].translate(form[0].getX(), ckp[5].getY() + ckp[0].getHeight());
		ckp[7].translate(ckp[0].getX() + ckp[0].getWidth(), ckp[6].getY());
		ckp[8].translate(ckp[1].getX() + ckp[1].getWidth(), ckp[6].getY());
		//----------------------------------------------------------------------
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		thread.setRunning(false);
	}

	//----------------------------------------------------------------------
	//	INPUT (TOUCH)
	//----------------------------------------------------------------------
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch(game_state) {
		case game:
			if (event.getAction() == MotionEvent.ACTION_MOVE) {

				line.addPoint(event.getX(), event.getY());

				for(int i = 0; i <= 8; i++) {
					ckp[i].handleActionDown((int)event.getX(), (int)event.getY());

					if(ckp[i].isTouched()) {
						form[random_int].delCKP(i);
					}
				}
			}

			if (event.getAction() == MotionEvent.ACTION_UP) {
				line.reset();

				if(form[random_int].noCKP()) {
					form[random_int].getCKP();
					random_int = next_random_int;

					do {
						next_random_int = random.nextInt(20);
					} while(next_random_int == random_int);

					SCORE++;

					//	DOUBLE POINTS	------------------------------------------------------------
					if(pro)
						SCORE++;
					//	DOUBLE POINTS	------------------------------------------------------------

					time_start = System.currentTimeMillis();

					if(SCORE < 3)			{	time_begin = 2500;	}
					else if(SCORE <= 20)	{	time_begin = 2200;	}
					else if(SCORE <= 40)	{	time_begin = 2000;	}
					else if(SCORE <= 60)	{	time_begin = 1800;	}
					else if(SCORE <= 80)	{	time_begin = 1300;	}
					else if(SCORE <= 90)	{	time_begin = 1100;	}
					else if(SCORE <= 100)	{	time_begin = 1000;	}
					else if(SCORE <= 150)	{	time_begin = 800;	}
					else if(SCORE <= 200)	{	time_begin = 675;	}
					else					{	time_begin = 550;	}

					time_end = time_begin + form[random_int].getCkpCount() * 75;
					sounds.play(sDing, 0.15f, 0.15f, 0, 0, 1.5f);

				} else {
					form[random_int].getCKP();
				}
			}
			break;

		case menu:
			btn_play.handleActionDown(event.getX(), event.getY());
			btn_score.handleActionDown(event.getX(), event.getY());
			btn_bubble.handleActionDown(event.getX(), event.getY());

			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				btn_play_clicked.handleActionDown(event.getX(), event.getY());
				btn_score_clicked.handleActionDown(event.getX(), event.getY());
			}

			if(event.getAction() == MotionEvent.ACTION_UP) {
				btn_play_clicked.setTouched(false);
				btn_score_clicked.setTouched(false);

				if(btn_bubble.isTouched() && showBubble) {
					sounds.play(sClick, 1.0f, 1.0f, 0, 0, 1.5f);
					showBubble = false;
					break;
				}

				if(btn_score.isTouched()) {
					sounds.play(sClick, 1.0f, 1.0f, 0, 0, 1.5f);
					game_state = state.score;
				}

				//----------------------------------------------------------------------
				//	NEW GAME
				//----------------------------------------------------------------------
				if(btn_play.isTouched()) {
					sounds.play(sClick, 1.0f, 1.0f, 0, 0, 1.5f);

					//	set new random Forms
					random_int = random.nextInt(20);
					do {
						next_random_int = random.nextInt(20);
					} while(next_random_int == random_int);

					//	renew all ckp's
					for(int i = 0; i <= 19; i++)
						form[i].getCKP();

					line.reset();

					// set score and timer
					SCORE = 0;
					time_start = System.currentTimeMillis();
					game_state = state.game;
				}
			}

			break;

		case score:
			if (event.getAction() == MotionEvent.ACTION_UP) {
				sounds.play(sClick, 1.0f, 1.0f, 0, 0, 1.5f);
				game_state = state.menu;
			}
			break;

		case postGame:
			if (event.getAction() == MotionEvent.ACTION_DOWN)
				postGame_delay = false;

			if (event.getAction() == MotionEvent.ACTION_UP && !postGame_delay) {
				sounds.play(sClick, 1.0f, 1.0f, 0, 0, 1.5f);
				postGame_delay = true;
				game_state = state.menu;
			}	
			break;

		case med:
			if (event.getAction() == MotionEvent.ACTION_DOWN)
				postGame_delay = false;

			if (event.getAction() == MotionEvent.ACTION_UP && !postGame_delay) {
				sounds.play(sClick, 1.0f, 1.0f, 0, 0, 1.5f);
				postGame_delay = true;
				game_state = state.menu;
			}

			break;

		default:
			break;
		}
		return true;
	}

	//----------------------------------------------------------------------
	//	DRAW
	//----------------------------------------------------------------------
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.DKGRAY);
		switch(game_state) {
		case menu:
			canvas.drawColor(Color.RED);
			//bg_main.draw(canvas, WIDTH, HEIGHT);

			if(!pro)
				btn_logo.draw(canvas);
			else
				btn_logo_pro.draw(canvas);

			btn_play.draw(canvas);
			btn_score.draw(canvas);
			if(btn_play_clicked.isTouched())
				btn_play_clicked.draw(canvas);
			if(btn_score_clicked.isTouched())
				btn_score_clicked.draw(canvas);
			if(!pro && showBubble) {
				btn_bubble.draw(canvas);
			}

			break;

		case game:
			time_elapsed = System.currentTimeMillis() - time_start;

			bg_ingame.draw(canvas, WIDTH, HEIGHT, 0, 0);

			if(pro)
				for(int i = 0; i <= 8; i++)	if(form[random_int].isCKP(i))	ckp[i].draw(canvas);

			form[random_int].draw(canvas);

			pb_bg.draw(canvas);
			pb_time.draw(canvas, (WIDTH * 0.475f / 100.0f) * ((int) (( (float) time_elapsed / time_end) * 100)));
			top_bar.draw(canvas);
			canvas.drawText("" + SCORE, WIDTH * 0.15f, HEIGHT * 0.075f + WIDTH / 40, scoreText);
			miniform[next_random_int].draw(canvas);

			line.draw(canvas);

			if((System.currentTimeMillis() - time_start) >= time_end) {
				time_start = 0;
				time_end = 2500;

				if(SCORE > MainActivity.highscore) {
					MainActivity.highscore = SCORE;
				}

				postGame_delay = true;

				if(SCORE >= 100 && !pro) {
					pro = true;
					game_state = state.med;
				}
				else
					game_state = state.postGame;

				break;
			}

			break;

		case score:
			bg_main.draw(canvas, WIDTH, HEIGHT);

			if(!pro)
				btn_logo.draw(canvas);
			else
				btn_logo_pro.draw(canvas);

			btn_logo_score.draw(canvas);
			canvas.drawText("" + MainActivity.highscore, WIDTH / 2, HEIGHT * 0.62f, highscoreText);
			break;

		case postGame:
			bg_main.draw(canvas, WIDTH, HEIGHT);
			btn_logo_score.draw(canvas);
			canvas.drawText("" + SCORE, WIDTH / 2, HEIGHT * 0.62f, highscoreText);
			break;

		case med:
			bg_main.draw(canvas, WIDTH, HEIGHT);
			btn_med.draw(canvas);
			btn_bubble_med.draw(canvas);
			break;

		default:
			canvas.drawColor(Color.RED);
			break;
		}
	}
}