package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.imageio.ImageIO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

@Controller
public class CaptchaController {

    private static final int WIDTH = 160;
    private static final int HEIGHT = 40;
    private static final int CODE_COUNT = 6;
    private static final int LINE_COUNT = 20;

    @GetMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg");

        // 创建内存中的图像
        BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bufferedImage.createGraphics();

        // 设置背景色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // 设置字体
        Font font = new Font("Fixedsys", Font.BOLD, 20);
        g.setFont(font);

        Random random = new Random();

        // 添加干扰线
        for (int i = 0; i < LINE_COUNT; i++) {
            int xs = random.nextInt(WIDTH);
            int ys = random.nextInt(HEIGHT);
            int xe = xs + random.nextInt(WIDTH / 8);
            int ye = ys + random.nextInt(HEIGHT / 8);
            g.setColor(getRandomColor());
            g.drawLine(xs, ys, xe, ye);
        }

        // 添加随机字符
        String code = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder captchaCode = new StringBuilder();
        for (int i = 0; i < CODE_COUNT; i++) {
            String strRand = String.valueOf(code.charAt(random.nextInt(code.length())));
            g.setColor(getRandomColor());
            g.drawString(strRand, 15 * i + 6, 24);
            captchaCode.append(strRand);
        }

        g.dispose();

        // 将验证码存入session
        request.getSession().setAttribute("captcha", captchaCode.toString());

        // 输出图像
        ImageIO.write(bufferedImage, "jpg", response.getOutputStream());
    }

    private Color getRandomColor() {
        Random random = new Random();
        float f = random.nextFloat();
        if (f < 0.5) {
            return Color.BLACK;
        } else {
            return new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
        }
    }
}