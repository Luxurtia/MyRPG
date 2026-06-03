package com.gema.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AssetManager {
    private final Map<String, BufferedImage> imageCache = new HashMap<>();      // 이미지 캐시(한번 로드시 재사용가능)

    public BufferedImage getImage(String key) {
        if(imageCache.containsKey(key)) {           // 이미 로드된 이미지이면 캐시에서 반환
            return imageCache.get(key);
        }

        String path = "/assets/images" + key + ".png";     // 리소스 경로
        try(InputStream is = getClass().getResourceAsStream(path)) {
            if(is == null) {
                System.err.println("[Asset_manager] 파일을 찾을 수 없음" + path);
                return null;
            }

            BufferedImage img = ImageIO.read(is);
            imageCache.put(key, img);       // 캐시에 이미지 저장
            return img;
        } catch(IOException e) {
            System.err.println("[AssetManager] 로드 실패" + path);
            return null;
        }
    }

    public BufferedImage cropSprite(String sheetKey, int col, int row, int tileW, int tileH) {
        BufferedImage sheet = getImage(sheetKey);
        if(sheet == null) {
            return null;
        }
        return sheet.getSubimage(col * tileW, row * tileH, tileW, tileH);       // 시트에서 특정 부분만 잘라냄
    }

    public void clearCache() {      // 캐시 비우기
        imageCache.clear();
    }

}
