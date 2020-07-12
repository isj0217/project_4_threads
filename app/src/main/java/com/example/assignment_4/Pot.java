package com.example.assignment_4;

import android.widget.Toast;

public class Pot {

    private int stage = 0;
    /**
     * stage는 냄비의 '단계'를 나타내는 것으로,
     * 0단계는 <텅 빈 냄비>,
     * 1단계는 <차가운 물이 들어있는 상태>,
     * 2단계는 <물이 끓는 상태>,
     * 3단계는 <물이 끓는 상태 + 라면>,
     * 4단계는 <물이 끓는 상태 + 라면 + 스프>,
     * 5단계는 <물이 끓는 상태 + 라면 + 스프 + 계란> 이 들어있는 상태를 뜻합니다.
     *
     * -1단계는 이미 냄비가 완전히 타버린 상태로, 더 이상의 재료첨가가 불가능합니다.
     *
     * 물이 끓기 시작하는 2단계 이후부터 <라면>을 냄비에 넣을 수 있습니다.
     *
     * 라면, 스프, 계란은 반드시 순서대로 투하되어야 하며, 계란까지 넣은 후에는 바로 라면을 완성(제출)할 수 있습니다.
     * */
    private boolean almost_burn = false;
    private boolean burn = false;



    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public boolean isAlmost_burn() {
        return almost_burn;
    }

    public void setAlmost_burn(boolean almost_burn) {
        this.almost_burn = almost_burn;
    }

    public boolean isBurn() {
        return burn;
    }

    public void setBurn(boolean burn) {
        this.burn = burn;
    }



    public boolean isSalable() {                    // 계란까지 넣은 5단계이고, 완전히 타지 않았을 때만 팔 수 있음
        if (Pot.this.getStage() == 5 && (!Pot.this.isBurn())){
            return true;
        }else
            return false;
    }

    public boolean isTrash() {                      // 완전히 타야만 버릴 수 있음(쓰레기로 인식함)
        return Pot.this.isBurn();
    }

}
