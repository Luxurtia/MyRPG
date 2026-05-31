package com.gema.system;

public enum Action {       // 키보드 동작 매핑용
      // enum => 미리 정해진 선택지들만 저장하는 타입

    // 이동
    MOVE_UP,
    MOVE_DOWN,
    MOVE_LEFT,
    MOVE_RIGHT,


    // 플레이어 행동
    TALK,       // NPC등과 상호작용
    INVENTORY,  // 인벤토리


    // UI       // 타이틀 및 인게임 인벤 조작용
    UI_UP,
    UI_DOWN,
    UI_LEFT,
    UI_RIGHT,
    UI_SELECT,
    UI_BACK,
    MENU,

}
