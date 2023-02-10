package org.vizzoid.utils.security;

import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Captcha {

    private static final StringEncrypter encrypter = StringEncrypter.create(Hashing.MD5);
    private final byte[] target;
    private final @NotNull File image;

    /**
     * @throws CaptchaException when file cannot be produced after 10 attempts or is stopped by SecurityManager
     */
    public Captcha() throws CaptchaException {
        this(generateRandomString());
    }

    /**
     * @throws CaptchaException when file cannot be produced after 10 attempts or is stopped by SecurityManager
     */
    public Captcha(String string) throws CaptchaException {
        this.target = encrypter.encrypt(string);

        // inspired by http://www.java2s.com/example/java/2d-graphics/create-captcha-image.html

        int width = 160;
        int height = 80;
        Random random = ThreadLocalRandom.current();

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);

        Font font = new Font("Arial", Font.BOLD, 25);
        graphics.setFont(font);
        GlyphVector vector = font.createGlyphVector(graphics.getFontRenderContext(), string);
        graphics.translate(25, 49);

        for (int i = 0, n = vector.getNumGlyphs(); i < n; i++) {
            Point2D p = vector.getGlyphPosition(i);
            double x = p.getX();
            double y = p.getY();

            int code = random.nextInt(235);
            graphics.setColor(new Color(code, code, code));

            double angle = random.nextInt(360);
            AffineTransform rotate = AffineTransform.getTranslateInstance(x, y);
            rotate.rotate(Math.toRadians(angle));
            rotate.translate(x, y);
            Shape finalShape = rotate.createTransformedShape(vector.getGlyphOutline(i));

            graphics.fill(finalShape);

            /*graphics.drawString(c,
                (xDiff * i) + (xDiff / 2),
                centerY);*/
        }

        graphics.dispose();
        this.image = convertToFile(image);
    }

    public @NotNull File getImage() {
        return image;
    }

    public boolean matches(String attempt) {
        return Arrays.equals(target, encrypter.encrypt(attempt));
    }

    public static @NotNull File convertToFile(@NotNull RenderedImage image) throws CaptchaException {
        return convertToFile(image, 0);
    }

    public static @NotNull File convertToFile(@NotNull RenderedImage image, int attempt) throws CaptchaException {
        if (attempt > 10) throw new CaptchaException("Could not produce Captcha File after 10 attempts");
        File tempImage = null;
        try {
            tempImage = File.createTempFile("captcha-image", ".png");
            ImageIO.write(image, "PNG", tempImage);
            return tempImage;
        } catch (SecurityException s) {
            throw new CaptchaException(s); // do not continue if stopped by security
        } catch (Exception e) { // retry until success
            if (tempImage != null) {
                //noinspection ResultOfMethodCallIgnored
                tempImage.delete();
            }
            return convertToFile(image, attempt + 1);
        }
    }


    public static @NotNull String generateRandomString() {
        return RandomStringUtils.randomAlphanumeric(5);
    }

}
