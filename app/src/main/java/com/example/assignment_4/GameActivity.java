package com.example.assignment_4;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class GameActivity extends AppCompatActivity implements View.OnTouchListener {

    private ImageView mIV_ramen_1, mIV_ramen_2, mIV_ramen_3, mIV_ramen_4, mIV_ramen_5, mIV_ramen_6,
            mIV_egg_1, mIV_egg_2, mIV_egg_3,
            mIV_powder_1, mIV_powder_2, mIV_powder_3, mIV_powder_4,
            mIV_water_1, mIV_water_2, mIV_water_3, mIV_water_4, mIV_water_5,
            mIV_pot_1, mIV_pot_2, mIV_pot_3, mIV_pot_4;

    private TextView mTv_game_timer, mTv_game_score;
    private CountDownTimer countDownTimer;
    private boolean flag_popUpShown;
    private boolean exit_thread_pot_1, exit_thread_pot_2, exit_thread_pot_3, exit_thread_pot_4;
    private boolean flag_salable_pot_1, flag_salable_pot_2, flag_salable_pot_3, flag_salable_pot_4;
    private boolean flag_isTrash_pot_1, flag_isTrash_pot_2, flag_isTrash_pot_3, flag_isTrash_pot_4;

    private Pot pot_1, pot_2, pot_3, pot_4;

    private float xPos, yPos;
    private float oldX, oldY;
    private float dropX, dropY;

    private int target_pot_number;
    private int mTotalScore;
    private String isHolding;

    private Thread thread_pot_1, thread_pot_2, thread_pot_3, thread_pot_4;

    private Button mBtn_sell, mBtn_dump;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);

        init();
    }

    @Override
    protected void onPause() {
        super.onPause();
        countDownTimer.cancel();
    }

    @Override
    protected void onRestart() {
        if (!flag_popUpShown) {
            buildPopup();
        }
        super.onRestart();
    }


    /**
     * onCreate에서 init() 메서드를 통해 각기 다른 메서드들을 호출하여
     * 뷰 바인딩 / 터치 리스너 부착 / 냄비 객체 생성 / 냄비 상태 초기화 / 플래그 초기화 / 4개의 쓰레드 초기화 / 타이머 초기화 / 버튼 초기화 등을 진행합니다.
     */
    private void init() {
        bindViewsById();
        setTouchListenersOnItems();

        declarePots();

        initPot_1_States();
        initPot_2_States();
        initPot_3_States();
        initPot_4_States();

        initFlags();
        initTimer();
        initButtons();

        isHolding = "NOTHING";
        target_pot_number = 0;

        mTotalScore = 0;
    }


    private void bindViewsById() {
        mIV_water_1 = findViewById(R.id.water_1);
        mIV_water_2 = findViewById(R.id.water_2);
        mIV_water_3 = findViewById(R.id.water_3);
        mIV_water_4 = findViewById(R.id.water_4);
        mIV_water_5 = findViewById(R.id.water_5);

        mIV_ramen_1 = findViewById(R.id.ramen_1);
        mIV_ramen_2 = findViewById(R.id.ramen_2);
        mIV_ramen_3 = findViewById(R.id.ramen_3);
        mIV_ramen_4 = findViewById(R.id.ramen_4);
        mIV_ramen_5 = findViewById(R.id.ramen_5);
        mIV_ramen_6 = findViewById(R.id.ramen_6);

        mIV_powder_1 = findViewById(R.id.powder_1);
        mIV_powder_2 = findViewById(R.id.powder_2);
        mIV_powder_3 = findViewById(R.id.powder_3);
        mIV_powder_4 = findViewById(R.id.powder_4);

        mIV_egg_1 = findViewById(R.id.egg_1);
        mIV_egg_2 = findViewById(R.id.egg_2);
        mIV_egg_3 = findViewById(R.id.egg_3);

        mIV_pot_1 = findViewById(R.id.pot_1);
        mIV_pot_2 = findViewById(R.id.pot_2);
        mIV_pot_3 = findViewById(R.id.pot_3);
        mIV_pot_4 = findViewById(R.id.pot_4);

        mTv_game_score = findViewById(R.id.tv_game_score);
        mTv_game_timer = findViewById(R.id.tv_game_timer);

        mBtn_sell = findViewById(R.id.btn_sell);
        mBtn_dump = findViewById(R.id.btn_dump);

    }

    private void setTouchListenersOnItems() {
        mIV_water_1.setOnTouchListener(this);
        mIV_water_2.setOnTouchListener(this);
        mIV_water_3.setOnTouchListener(this);
        mIV_water_4.setOnTouchListener(this);
        mIV_water_5.setOnTouchListener(this);

        mIV_ramen_1.setOnTouchListener(this);
        mIV_ramen_2.setOnTouchListener(this);
        mIV_ramen_3.setOnTouchListener(this);
        mIV_ramen_4.setOnTouchListener(this);
        mIV_ramen_5.setOnTouchListener(this);
        mIV_ramen_6.setOnTouchListener(this);

        mIV_powder_1.setOnTouchListener(this);
        mIV_powder_2.setOnTouchListener(this);
        mIV_powder_3.setOnTouchListener(this);
        mIV_powder_4.setOnTouchListener(this);

        mIV_egg_1.setOnTouchListener(this);
        mIV_egg_2.setOnTouchListener(this);
        mIV_egg_3.setOnTouchListener(this);
    }

    private void declarePots() {
        pot_1 = new Pot();
        pot_2 = new Pot();
        pot_3 = new Pot();
        pot_4 = new Pot();
    }

    private void initPot_1_States() {
        mIV_pot_1.setImageResource(R.drawable.pot_empty);
        pot_1.setStage(0);
        pot_1.setAlmost_burn(false);
        pot_1.setBurn(false);
    }

    private void initPot_2_States() {
        mIV_pot_2.setImageResource(R.drawable.pot_empty);
        pot_2.setStage(0);
        pot_2.setAlmost_burn(false);
        pot_2.setBurn(false);
    }

    private void initPot_3_States() {
        mIV_pot_3.setImageResource(R.drawable.pot_empty);
        pot_3.setStage(0);
        pot_3.setAlmost_burn(false);
        pot_3.setBurn(false);
    }

    private void initPot_4_States() {
        mIV_pot_4.setImageResource(R.drawable.pot_empty);
        pot_4.setStage(0);
        pot_4.setAlmost_burn(false);
        pot_4.setBurn(false);
    }

    private void initFlags() {
        flag_popUpShown = false;

        flag_salable_pot_1 = false;
        flag_salable_pot_2 = false;
        flag_salable_pot_3 = false;
        flag_salable_pot_4 = false;

        flag_isTrash_pot_1 = false;
        flag_isTrash_pot_2 = false;
        flag_isTrash_pot_3 = false;
        flag_isTrash_pot_4 = false;

        exit_thread_pot_1 = false;
        exit_thread_pot_2 = false;
        exit_thread_pot_3 = false;
        exit_thread_pot_4 = false;
    }

    private void initTimer() {
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTv_game_timer.setText(String.format(Locale.getDefault(), "남은 시간: %d 초", (millisUntilFinished / 1000L) + 1));
            }

            @Override
            public void onFinish() {
                mTv_game_timer.setText("종료");

                Intent intent = new Intent(GameActivity.this, ScoreActivity.class);
                intent.putExtra("finalScore", mTotalScore);
                startActivity(intent);
                finish();
            }
        };
        countDownTimer.start();
    }

    private void initButtons() {
        mBtn_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                flag_salable_pot_1 = pot_1.isSalable();
                flag_salable_pot_2 = pot_2.isSalable();
                flag_salable_pot_3 = pot_3.isSalable();
                flag_salable_pot_4 = pot_4.isSalable();

                if (flag_salable_pot_1) {
                    mTotalScore += evaluatePotQuality(pot_1);
                    updateScore();
                    initPot_1_States();
                    thread_pot_1.interrupt();
                }

                if (flag_salable_pot_2) {
                    mTotalScore += evaluatePotQuality(pot_2);
                    updateScore();
                    initPot_2_States();
                    thread_pot_2.interrupt();
                }

                if (flag_salable_pot_3) {
                    mTotalScore += evaluatePotQuality(pot_3);
                    updateScore();
                    initPot_3_States();
                    thread_pot_3.interrupt();
                }

                if (flag_salable_pot_4) {
                    mTotalScore += evaluatePotQuality(pot_4);
                    updateScore();
                    initPot_4_States();
                    thread_pot_4.interrupt();
                }

            }
        });

        mBtn_dump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                flag_isTrash_pot_1 = pot_1.isTrash();
                flag_isTrash_pot_2 = pot_2.isTrash();
                flag_isTrash_pot_3 = pot_3.isTrash();
                flag_isTrash_pot_4 = pot_4.isTrash();

                if (flag_isTrash_pot_1) {
                    initPot_1_States();
                }

                if (flag_isTrash_pot_2) {
                    initPot_2_States();
                }

                if (flag_isTrash_pot_3) {
                    initPot_3_States();
                }

                if (flag_isTrash_pot_4) {
                    initPot_4_States();
                }
            }
        });
    }

    /**
     * init()메서드를 통한 초기화 종료
     */


    private void updateScore() {
        mTv_game_score.setText("현재 금액: " + mTotalScore + "원");
    }


    /**
     * user가 손가락을 놓는 순간, 가져온 ingredients를 통해 stage를 업데이트해주고 그림을 변경해주는 메서드
     */
    private void updatePotState(int potNum, String ingredients) {

        switch (potNum) {

            /**
             * 가져온 것이 water일 때: 일단 물 부은 모양으로 바꾸며 stage 1로 바꿔주고 쓰레드 start
             * 가져온 것이 ramen일 때: stage 2(끓는물)인지 확인하고, 냄비 상태 2가지에 따라 이미지 바꿔줌(ramen / 그을린 ramen)
             * 가져온 것이 powder일 때: stage 3(끓는물 + 라면)인지 확인하고, 냄비 상태 2가지에 따라 이미지 바꿔줌(ramen + powder / 그을린 ramen + powder)
             * 가져온 것이 egg일 때: stage 4(끓는물 + 라면 + 스프)인지 확인하고, 냄비 상태 2가지에 따라 이미지 바꿔줌(ramen + powder + egg / 그을린 ramen + powder + egg)
             * */

            case 1:
                switch (ingredients) {
                    case "WATER":
                        if (pot_1.getStage() == 0) {
                            mIV_pot_1.setImageResource(R.drawable.pot_water);
                            pot_1.setStage(1);
                            exit_thread_pot_1 = false;
                            thread_pot_1 = new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    while (!exit_thread_pot_1) {
                                        exit_thread_pot_1 = true;

                                        try {
                                            Thread.sleep(500);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                        mIV_pot_1.setImageResource(R.drawable.pot_boiling);
                                        pot_1.setStage(2);

                                        try {
                                            Thread.sleep(8000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                            return;
                                        }

                                        pot_1.setAlmost_burn(true);
                                        int stageBeforeAlmostBurn = pot_1.getStage();

                                        switch (stageBeforeAlmostBurn) {
                                            case 2:
                                                mIV_pot_1.setImageResource(R.drawable.pot_almost_burn_with_water);
                                                break;
                                            case 3:
                                                mIV_pot_1.setImageResource(R.drawable.pot_almost_burn_with_water_and_ramen);
                                                break;
                                            case 4:
                                                mIV_pot_1.setImageResource(R.drawable.pot_almost_burn_with_water_and_ramen_and_powder);
                                                break;
                                            case 5:
                                                mIV_pot_1.setImageResource(R.drawable.pot_almost_burn_with_water_and_ramen_and_powder_and_egg);
                                                break;
                                        }

                                        try {
                                            Thread.sleep(4000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                            return;
                                        }

                                        pot_1.setBurn(true);
                                        int stageBeforeBurn = pot_1.getStage();

                                        switch (stageBeforeBurn) {
                                            case 2:
                                                mIV_pot_1.setImageResource(R.drawable.pot_burn);
                                                pot_1.setStage(-1);
                                                break;
                                            case 3:
                                                mIV_pot_1.setImageResource(R.drawable.pot_burn_with_water_and_ramen);
                                                pot_1.setStage(-1);
                                                break;
                                            case 4:
                                                mIV_pot_1.setImageResource(R.drawable.pot_burn_with_water_and_ramen_and_powder);
                                                pot_1.setStage(-1);
                                                break;
                                            case 5:
                                                mIV_pot_1.setImageResource(R.drawable.pot_burn_with_water_and_ramen_and_powder_and_egg);
                                                pot_1.setStage(-1);
                                                break;
                                        }
                                    }
                                }
                            });
                            thread_pot_1.start();
                        }
                        break;
                    case "RAMEN":
                        if (pot_1.getStage() == 0) {
                            Toast.makeText(this, "물을 먼저 넣어주세요", Toast.LENGTH_SHORT).show();
                        } else if (pot_1.getStage() == 1) {
                            Toast.makeText(this, "아직 물이 끓지 않았어요", Toast.LENGTH_SHORT).show();
                        } else if (pot_1.getStage() == 2) {
                            if (!pot_1.isAlmost_burn()) {
                                mIV_pot_1.setImageResource(R.drawable.pot_with_ramen);
                            } else {
                                mIV_pot_1.setImageResource(R.drawable.pot_almost_burn_with_water_and_ramen);
                            }
                            pot_1.setStage(3);
                        }
                        break;
                    case "POWDER":
                        if (pot_1.getStage() == 0) {
                            Toast.makeText(this, "물을 먼저 넣어주세요", Toast.LENGTH_SHORT).show();
                        } else if (pot_1.getStage() == 1) {
                            Toast.makeText(this, "아직 물이 끓지 않았어요", Toast.LENGTH_SHORT).show();
                        } else if (pot_1.getStage() == 2) {
                            Toast.makeText(this, "라면을 먼저 넣어주세요", Toast.LENGTH_SHORT).show();
                        } else if (pot_1.getStage() == 3) {
                            if (!pot_1.isAlmost_burn()) {
                                mIV_pot_1.setImageResource(R.drawable.pot_with_ramen_and_powder);
                            } else {
                                mIV_pot_1.setImageResource(R.drawable.pot_almost_burn_with_water_and_ramen_and_powder);
                            }
                            pot_1.setStage(4);
                        }
                        break;
                    case "EGG":
                        if (pot_1.getStage() == 0) {
                            Toast.makeText(this, "물을 먼저 넣어주세요", Toast.LENGTH_SHORT).show();
                        } else if (pot_1.getStage() == 1) {
                            Toast.makeText(this, "아직 물이 끓지 않았어요", Toast.LENGTH_SHORT).show();
                        } else if (pot_1.getStage() == 2) {
                            Toast.makeText(this, "라면을 먼저 넣어주세요", Toast.LENGTH_SHORT).show();
                        } else if (pot_1.getStage() == 3) {
                            Toast.makeText(this, "스프를 먼저 넣어주세요", Toast.LENGTH_SHORT).show();
                        } else if (pot_1.getStage() == 4) {
                            if (!pot_1.isAlmost_burn()) {
                                mIV_pot_1.setImageResource(R.drawable.pot_with_ramen_and_powder_and_egg);
                            } else {
                                mIV_pot_1.setImageResource(R.drawable.pot_almost_burn_with_water_and_ramen_and_powder_and_egg);
                            }
                            pot_1.setStage(5);
                        }
                        break;
                    default:
                        break;
                }
                break;

            case 2:
                switch (ingredients) {
                    case "WATER":
                        if (pot_2.getStage() == 0) {
                            mIV_pot_2.setImageResource(R.drawable.pot_water);
                            pot_2.setStage(1);
                            exit_thread_pot_2 = false;
                            thread_pot_2 = new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    while (!exit_thread_pot_2) {
                                        exit_thread_pot_2 = true;

                                        try {
                                            Thread.sleep(500);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                        mIV_pot_2.setImageResource(R.drawable.pot_boiling);
                                        pot_2.setStage(2);

                                        try {
                                            Thread.sleep(8000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                            return;
                                        }

                                        pot_2.setAlmost_burn(true);
                                        int stageBeforeAlmostBurn = pot_2.getStage();

                                        switch (stageBeforeAlmostBurn) {
                                            case 2:
                                                mIV_pot_2.setImageResource(R.drawable.pot_almost_burn_with_water);
                                                break;
                                            case 3:
                                                mIV_pot_2.setImageResource(R.drawable.pot_almost_burn_with_water_and_ramen);
                                                break;
                                            case 4:
                                                mIV_pot_2.setImageResource(R.drawable.pot_almost_burn_with_water_and_ramen_and_powder);
                                                break;
                                            case 5:
                                                mIV_pot_2.setImageResource(R.drawable.pot_almost_burn_with_water_and_ramen_and_powder_and_egg);
                                                break;
                                        }

                                        try {
                                            Thread.sleep(4000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                            return;
                                        }

                                        pot_2.setBurn(true);
                                        int stageBeforeBurn = pot_2.getStage();

                                        switch (stageBeforeBurn) {
                                            case 2:
                                                mIV_pot_2.setImageResource(R.drawable.pot_burn);
                                                pot_2.setStage(-1);
                                                break;
                                            case 3:
                                                mIV_pot_2.setImageResource(R.drawable.pot_burn_with_water_and_ramen);
                                                pot_2.setStage(-1);
                                                break;
                                            case 4:
                                                mIV_pot_2.setImageResource(R.drawable.pot_burn_with_water_and_ramen_and_powder);
                                                pot_2.setStage(-1);
                                                break;
                                            case 5:
                                                mIV_pot_2.setImageResource(R.drawable.pot_burn_with_water_and_ramen_and_powder_and_egg);
                                                pot_2.setStage(-1);
                                                break;
                                        }
                                    }
                                }
                            });
                            thread_pot_2.start();
                        }
                        break;
                    case "RAMEN":
                        if (pot_2.getStage() == 0) {
                            Toast.makeText(this, "물을 먼저 넣어주세요", Toast.LENGTH_SHORT).show();
                        } else if (pot_2.getStage() == 1) {
                            Toast.makeText(this, "아직 물이 끓지 않았어요", Toast.LENGTH_SHORT).show();
                        } else if (pot_2.getStage() == 2) {
                            if (!pot_2.isAlmost_burn()) {
                                mIV_pot_2.setImageResource(R.drawable.pot_with_ramen);
                            } else {
                                mIV_pot_2.setImageResource(R.drawable.pot_almost_burn_with_water_and_ramen);
                            }
                            pot_2.setStage(3);
                        }
                        break;
                    case "POWDER":
                        if (pot_2.getStage() == 0) {
                            Toast.makeText(this, "물을 먼저 넣어주세요", Toast.LENGTH_SHORT).show();
                        } else if (pot_2.getStage() == 1) {
                            Toast.makeText(this, "아직 물이 끓지 않았어요", Toast.LENGTH_SHORT).show();
                        } else if (pot_2.getStage() == 2) {
                            Toast.makeText(this, "라면을 먼저 넣어주세요", Toast.LENGTH_SHORT).show();
                        } else if (pot_2.getStage() == 3) {
                            if (!pot_2.isAlmost_burn()) {
                                mIV_pot_2.setImageResource(R.drawable.pot_with_ramen_and_powder);
                            } else {
                                mIV_pot_2.setImageResource(R.drawable.pot_almost_burn_with_water_and_ramen_and_powder);
                            }
                            pot_2.setStage(4);
                        }
                        break;
                    case "EGG":
                        if (pot_2.getStage() == 0) {
                            Toast.makeText(this, "물을 먼저 넣어주세요", Toast.LENGTH_SHORT).show();
                        } else if (pot_2.getStage() == 1) {
                            Toast.makeText(this, "아직 물이 끓지 않았어요", Toast.LENGTH_SHORT).show();
                        } else if (pot_2.getStage() == 2) {
                            Toast.makeText(this, "라면을 먼저 넣어주세요", Toast.LENGTH_SHORT).show();
                        } else if (pot_2.getStage() == 3) {
                            Toast.makeText(this, "스프를 먼저 넣어주세요", Toast.LENGTH_SHORT).show();
                        } else if (pot_2.getStage() == 4) {
                            if (!pot_2.isAlmost_burn()) {
                                mIV_pot_2.setImageResource(R.drawable.pot_with_ramen_and_powder_and_egg);
                            } else {
                                mIV_pot_2.setImageResource(R.drawable.pot_almost_burn_with_water_and_ramen_and_powder_and_egg);
                            }
                            pot_2.setStage(5);
                        }
                        break;
                    default:
                        break;
                }
                break;

            case 3:
                switch (ingredients) {
                    case "WATER":
                        if (pot_3.getStage() == 0) {
                            mIV_pot_3.setImageResource(R.drawable.pot_water);
                            pot_3.setStage(1);
                            exit_thread_pot_3 = false;
                            thread_pot_3 = new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    while (!exit_thread_pot_3) {
                                        exit_thread_pot_3 = true;

                                        try {
                                            Thread.sleep(500);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                        mIV_pot_3.setImageResource(R.drawable.pot_boiling);
                                        pot_3.setStage(2);

                                        try {
                                            Thread.sleep(8000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                            return;
                                        }

                                        pot_3.setAlmost_burn(true);
                                        int stageBeforeAlmostBurn = pot_3.getStage();

                                        switch (stageBeforeAlmostBurn) {
                                            case 2:
                                                mIV_pot_3.setImageResource(R.drawable.pot_almost_burn_with_water);
                                                break;
                                            case 3:
                                                mIV_pot_3.setImageResource(R.drawable.pot_almost_burn_with_water_and_ramen);
                                                break;
                                            case 4:
                                                mIV_pot_3.setImageResource(R.drawable.pot_almost_burn_with_water_and_ramen_and_powder);
                                                break;
                                            case 5:
                                                mIV_pot_3.setImageResource(R.drawable.pot_almost_burn_with_water_and_ramen_and_powder_and_egg);
                                                break;
                                        }

                                        try {
                                            Thread.sleep(4000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                            return;
                                        }

                                        pot_3.setBurn(true);
                                        int stageBeforeBurn = pot_3.getStage();

                                        switch (stageBeforeBurn) {
                                            case 2:
                                                mIV_pot_3.setImageResource(R.drawable.pot_burn);
                                                pot_3.setStage(-1);
                                                break;
                                            case 3:
                                                mIV_pot_3.setImageResource(R.drawable.pot_burn_with_water_and_ramen);
                                                pot_3.setStage(-1);
                                                break;
                                            case 4:
                                                mIV_pot_3.setImageResource(R.drawable.pot_burn_with_water_and_ramen_and_powder);
                                                pot_3.setStage(-1);
                                                break;
                                            case 5:
                                                mIV_pot_3.setImageResource(R.drawable.pot_burn_with_water_and_ramen_and_powder_and_egg);
                                                pot_3.setStage(-1);
                                                break;
                                        }
                                    }
                                }
                            });
                            thread_pot_3.start();
                        }
                        break;
                    case "RAMEN":
                        if (pot_3.getStage() == 0) {
                            Toast.makeText(this, "물을 먼저 넣어주세요", Toast.LENGTH_SHORT).show();
                        } else if (pot_3.getStage() == 1) {
                            Toast.makeText(this, "아직 물이 끓지 않았어요", Toast.LENGTH_SHORT).show();
                        } else if (pot_3.getStage() == 2) {
                            if (!pot_3.isAlmost_burn()) {
                                mIV_pot_3.setImageResource(R.drawable.pot_with_ramen);
                            } else {
                                mIV_pot_3.setImageResource(R.drawable.pot_almost_burn_with_water_and_ramen);
                            }
                            pot_3.setStage(3);
                        }
                        break;
                    case "POWDER":
                        if (pot_3.getStage() == 0) {
                            Toast.makeText(this, "물을 먼저 넣어주세요", Toast.LENGTH_SHORT).show();
                        } else if (pot_3.getStage() == 1) {
                            Toast.makeText(this, "아직 물이 끓지 않았어요", Toast.LENGTH_SHORT).show();
                        } else if (pot_3.getStage() == 2) {
                            Toast.makeText(this, "라면을 먼저 넣어주세요", Toast.LENGTH_SHORT).show();
                        } else if (pot_3.getStage() == 3) {
                            if (!pot_3.isAlmost_burn()) {
                                mIV_pot_3.setImageResource(R.drawable.pot_with_ramen_and_powder);
                            } else {
                                mIV_pot_3.setImageResource(R.drawable.pot_almost_burn_with_water_and_ramen_and_powder);
                            }
                            pot_3.setStage(4);
                        }
                        break;
                    case "EGG":
                        if (pot_3.getStage() == 0) {
                            Toast.makeText(this, "물을 먼저 넣어주세요", Toast.LENGTH_SHORT).show();
                        } else if (pot_3.getStage() == 1) {
                            Toast.makeText(this, "아직 물이 끓지 않았어요", Toast.LENGTH_SHORT).show();
                        } else if (pot_3.getStage() == 2) {
                            Toast.makeText(this, "라면을 먼저 넣어주세요", Toast.LENGTH_SHORT).show();
                        } else if (pot_3.getStage() == 3) {
                            Toast.makeText(this, "스프를 먼저 넣어주세요", Toast.LENGTH_SHORT).show();
                        } else if (pot_3.getStage() == 4) {
                            if (!pot_3.isAlmost_burn()) {
                                mIV_pot_3.setImageResource(R.drawable.pot_with_ramen_and_powder_and_egg);
                            } else {
                                mIV_pot_3.setImageResource(R.drawable.pot_almost_burn_with_water_and_ramen_and_powder_and_egg);
                            }
                            pot_3.setStage(5);
                        }
                        break;
                    default:
                        break;
                }
                break;

            case 4:
                switch (ingredients) {
                    case "WATER":
                        if (pot_4.getStage() == 0) {
                            mIV_pot_4.setImageResource(R.drawable.pot_water);
                            pot_4.setStage(1);
                            exit_thread_pot_4 = false;
                            thread_pot_4 = new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    System.out.println("run 안에 들어옴");

                                    while (!exit_thread_pot_4) {
                                        exit_thread_pot_4 = true;

                                        System.out.println("while 안에 들어옴");

                                        try {
                                            Thread.sleep(500);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                        mIV_pot_4.setImageResource(R.drawable.pot_boiling);
                                        pot_4.setStage(2);

                                        try {
                                            Thread.sleep(8000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                            return;
                                        }

                                        pot_4.setAlmost_burn(true);
                                        int stageBeforeAlmostBurn = pot_4.getStage();

                                        switch (stageBeforeAlmostBurn) {
                                            case 2:
                                                mIV_pot_4.setImageResource(R.drawable.pot_almost_burn_with_water);
                                                break;
                                            case 3:
                                                mIV_pot_4.setImageResource(R.drawable.pot_almost_burn_with_water_and_ramen);
                                                break;
                                            case 4:
                                                mIV_pot_4.setImageResource(R.drawable.pot_almost_burn_with_water_and_ramen_and_powder);
                                                break;
                                            case 5:
                                                mIV_pot_4.setImageResource(R.drawable.pot_almost_burn_with_water_and_ramen_and_powder_and_egg);
                                                break;
                                        }

                                        try {
                                            Thread.sleep(4000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                            return;
                                        }

                                        pot_4.setBurn(true);
                                        int stageBeforeBurn = pot_4.getStage();

                                        switch (stageBeforeBurn) {
                                            case 2:
                                                mIV_pot_4.setImageResource(R.drawable.pot_burn);
                                                pot_4.setStage(-1);
                                                break;
                                            case 3:
                                                mIV_pot_4.setImageResource(R.drawable.pot_burn_with_water_and_ramen);
                                                pot_4.setStage(-1);
                                                break;
                                            case 4:
                                                mIV_pot_4.setImageResource(R.drawable.pot_burn_with_water_and_ramen_and_powder);
                                                pot_4.setStage(-1);
                                                break;
                                            case 5:
                                                mIV_pot_4.setImageResource(R.drawable.pot_burn_with_water_and_ramen_and_powder_and_egg);
                                                pot_4.setStage(-1);
                                                break;
                                        }
                                    }
                                }
                            });
                            thread_pot_4.start();
                        }
                        break;
                    case "RAMEN":
                        if (pot_4.getStage() == 0) {
                            Toast.makeText(this, "물을 먼저 넣어주세요", Toast.LENGTH_SHORT).show();
                        } else if (pot_4.getStage() == 1) {
                            Toast.makeText(this, "아직 물이 끓지 않았어요", Toast.LENGTH_SHORT).show();
                        } else if (pot_4.getStage() == 2) {
                            if (!pot_4.isAlmost_burn()) {
                                mIV_pot_4.setImageResource(R.drawable.pot_with_ramen);
                            } else {
                                mIV_pot_4.setImageResource(R.drawable.pot_almost_burn_with_water_and_ramen);
                            }
                            pot_4.setStage(3);
                        }
                        break;
                    case "POWDER":
                        if (pot_4.getStage() == 0) {
                            Toast.makeText(this, "물을 먼저 넣어주세요", Toast.LENGTH_SHORT).show();
                        } else if (pot_4.getStage() == 1) {
                            Toast.makeText(this, "아직 물이 끓지 않았어요", Toast.LENGTH_SHORT).show();
                        } else if (pot_4.getStage() == 2) {
                            Toast.makeText(this, "라면을 먼저 넣어주세요", Toast.LENGTH_SHORT).show();
                        } else if (pot_4.getStage() == 3) {
                            if (!pot_4.isAlmost_burn()) {
                                mIV_pot_4.setImageResource(R.drawable.pot_with_ramen_and_powder);
                            } else {
                                mIV_pot_4.setImageResource(R.drawable.pot_almost_burn_with_water_and_ramen_and_powder);
                            }
                            pot_4.setStage(4);
                        }
                        break;
                    case "EGG":
                        if (pot_4.getStage() == 0) {
                            Toast.makeText(this, "물을 먼저 넣어주세요", Toast.LENGTH_SHORT).show();
                        } else if (pot_4.getStage() == 1) {
                            Toast.makeText(this, "아직 물이 끓지 않았어요", Toast.LENGTH_SHORT).show();
                        } else if (pot_4.getStage() == 2) {
                            Toast.makeText(this, "라면을 먼저 넣어주세요", Toast.LENGTH_SHORT).show();
                        } else if (pot_4.getStage() == 3) {
                            Toast.makeText(this, "스프를 먼저 넣어주세요", Toast.LENGTH_SHORT).show();
                        } else if (pot_4.getStage() == 4) {
                            if (!pot_4.isAlmost_burn()) {
                                mIV_pot_4.setImageResource(R.drawable.pot_with_ramen_and_powder_and_egg);
                            } else {
                                mIV_pot_4.setImageResource(R.drawable.pot_almost_burn_with_water_and_ramen_and_powder_and_egg);
                            }
                            pot_4.setStage(5);
                        }
                        break;
                    default:
                        break;
                }
                break;
        }

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:

                oldX = v.getX();
                oldY = v.getY();

                xPos = v.getX() - event.getRawX();
                yPos = v.getY() - event.getRawY();

                determineItemUserIsHolding(event);
                break;

            case MotionEvent.ACTION_MOVE:
                v.animate().x(event.getRawX() + xPos).y(event.getRawY() + yPos).setDuration(0).start();
                break;

            case MotionEvent.ACTION_UP:
                dropX = event.getRawX() + xPos;
                dropY = event.getRawY() + yPos;

                switch (isHolding) {

                    case "WATER":

                        /**
                         * 사용자가 터치를 떼면서 의도한 냄비가 어디일지 지정해줍니다.
                         * */

                        if (480 < dropY && dropY < 650) {     //일단 냄비 4개의 높이가 같으므로 y 값으로 한번 걸러줌

                            //물을 몇번 냄비에 부을 것인지 결정
                            if (45 < dropX && dropX < 205) {
                                target_pot_number = 1;
                            } else if (320 < dropX && dropX < 475) {
                                target_pot_number = 2;
                            } else if (665 < dropX && dropX < 805) {
                                target_pot_number = 3;
                            } else if (900 < dropX && dropX < 1070) {
                                target_pot_number = 4;
                            }

                            updatePotState(target_pot_number, "WATER");

                        }
                        v.animate().x(oldX).y(oldY).setDuration(100).start();

                        break;

                    case "RAMEN":

                        /**
                         * 사용자가 터치를 떼면서 의도한 냄비가 어디일지 지정해줍니다.
                         * */

                        if (480 < dropY && dropY < 650) {     //일단 냄비 4개의 높이가 같으므로 y 값으로 한번 걸러줌

                            //물을 몇번 냄비에 부을 것인지 결정
                            if (45 < dropX && dropX < 205) {
                                target_pot_number = 1;
                            } else if (320 < dropX && dropX < 475) {
                                target_pot_number = 2;
                            } else if (665 < dropX && dropX < 805) {
                                target_pot_number = 3;
                            } else if (900 < dropX && dropX < 1070) {
                                target_pot_number = 4;
                            }

                            updatePotState(target_pot_number, "RAMEN");
                        }
                        v.animate().x(oldX).y(oldY).setDuration(100).start();

                        break;

                    case "POWDER":

                        /**
                         * 사용자가 터치를 떼면서 의도한 냄비가 어디일지 지정해줍니다.
                         * */

                        if (480 < dropY && dropY < 650) {     //일단 냄비 4개의 높이가 같으므로 y 값으로 한번 걸러줌

                            //물을 몇번 냄비에 부을 것인지 결정
                            if (45 < dropX && dropX < 205) {
                                target_pot_number = 1;
                            } else if (320 < dropX && dropX < 475) {
                                target_pot_number = 2;
                            } else if (665 < dropX && dropX < 805) {
                                target_pot_number = 3;
                            } else if (900 < dropX && dropX < 1070) {
                                target_pot_number = 4;
                            }

                            updatePotState(target_pot_number, "POWDER");
                        }
                        v.animate().x(oldX).y(oldY).setDuration(100).start();
                        break;

                    case "EGG":

                        /**
                         * 사용자가 터치를 떼면서 의도한 냄비가 어디일지 지정해줍니다.
                         * */

                        if (480 < dropY && dropY < 650) {     //일단 냄비 4개의 높이가 같으므로 y 값으로 한번 걸러줌

                            //물을 몇번 냄비에 부을 것인지 결정
                            if (45 < dropX && dropX < 205) {
                                target_pot_number = 1;
                            } else if (320 < dropX && dropX < 475) {
                                target_pot_number = 2;
                            } else if (665 < dropX && dropX < 805) {
                                target_pot_number = 3;
                            } else if (900 < dropX && dropX < 1070) {
                                target_pot_number = 4;
                            }

                            updatePotState(target_pot_number, "EGG");
                        }
                        v.animate().x(oldX).y(oldY).setDuration(100).start();
                        break;

                    default:
                        break;
                }
                isHolding = "NOTHING";

            default:
                return false;
        }
        return true;
    }

    private int evaluatePotQuality(Pot pot) {

        int price = 0;

        if (pot.isBurn()) {
            price = 0;                                          // 냄비가 완전히 탔으면 무조건 0원
        } else if (pot.isAlmost_burn()) {
            if (pot.getStage() == 5) {
                price = 500;
            }                                                   // 냄비가 그을렸지만 계란까지 다 넣었으면 500원
        } else {
            if (pot.getStage() == 5) {
                price = 1000;
            }
        }                                                       // 냄비가 멀쩡하고 계란까지 다 넣었으면 1000원

        return price;
    }

    public void buildPopup() {
        flag_popUpShown = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setTitle("진행중이던 게임이 있습니다").setMessage("다시 시작 하시겠습니까?").setCancelable(false);
        builder.setPositiveButton("끝내기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                flag_popUpShown = false;
                finish();
            }
        }).setNegativeButton("다시 하기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                flag_popUpShown = false;
                restartActivity(GameActivity.this);
            }
        });
        builder.create().show();

    }

    private void restartActivity(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, activity.getClass());
        activity.startActivity(intent);
        activity.finish();
    }

    private void determineItemUserIsHolding(MotionEvent event) {
        if (184 < event.getRawX() && event.getRawX() < 660 && 94 < event.getRawY() && event.getRawY() < 220) {
            isHolding = "RAMEN";
        } else if (800 < event.getRawX() && event.getRawX() < 1230 && 50 < event.getRawY() && event.getRawY() < 200) {
            isHolding = "EGG";
        } else if (158 < event.getRawX() && event.getRawX() < 672 && 324 < event.getRawY() && event.getRawY() < 500) {
            isHolding = "POWDER";
        } else if (820 < event.getRawX() && event.getRawX() < 1210 && 277 < event.getRawY() && event.getRawY() < 470) {
            isHolding = "WATER";
        } else {
            isHolding = "NOTHING";
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GameActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
