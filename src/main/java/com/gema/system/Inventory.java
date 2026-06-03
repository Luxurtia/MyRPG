package com.gema.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Inventory {
    private static final int MAX_SLOTS = 20;                    // 인벤 최대 칸
    private  final List<String> items = new ArrayList<>();      // 아이템 목록

    public boolean addItem(String Item) {           // 아이템 획득
        if(items.size() >= MAX_SLOTS) {     // 인벤이 가득 찼을때
            System.out.println("[Inventory] 가득참" + items + "추가 실패");
            return false;
        } else {
            items.add(Item);
            System.out.println("[Inventory] 추가" + items);
            return true;
        }
    }

    public boolean removeItem(String Item) {        // 아이템 제거
        boolean removed = items.remove(Item);
        if(removed) {
            System.out.println("[Inventory] 제거" + items);
        }
        return removed;
    }

    public boolean haveItem(String Item) {
        return items.contains(Item);
    }

    public String getItem(int index) {      // list에서 특정 위치(index)의 아이템을 꺼내주는 함수
        if(index < 0 || index >= items.size()) {    // index의 음수 및 범위초과 방지
            System.out.println("[Inventory] 어아탬 찾지 못함" + index);
            return null;
        }
        return items.get(index);
    }

    public List<String> getAllItems() {
        return Collections.unmodifiableList(items);     // 외부 수정방지를 위해 읽기 전용으로 반환
    }

    public int size() {         // 현재 아이템 수
        return items.size();
    }

    public boolean isFull() {   // 인벤이 가득 찾는지 여부 확인
        return items.size() >= MAX_SLOTS;
    }
}
