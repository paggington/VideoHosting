package com.example.videohosting.video_sampling;

import com.example.videohosting.files.DIRECTORY;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.model.ColorSpace;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.ColorUtil;
import org.jcodec.scale.RgbToBgr;
import org.jcodec.scale.Transform;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class VideoFrameExtractor implements DIRECTORY {
    public Resource getFirstFrame(Path path){
        File file=new File(path.toAbsolutePath().normalize().toUri());
        if(file.exists()){

            try {
                Picture frame= FrameGrab.getFrameFromFile(file,1);
                File file_extracted=File.createTempFile("extr_1"+file.getName().replaceAll("[.]", ""),
                        ".png", new File(DIR + "\\extracted"));

                ImageIO.write(toBufferedImage8Bit(frame), "png", file_extracted);
                System.out.println(file_extracted.getPath());
                Path path_resolved=Path.of(file_extracted.getPath()).toAbsolutePath().normalize();
                return new UrlResource(path_resolved.toUri());
            } catch (IOException | JCodecException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    private BufferedImage toBufferedImage8Bit(Picture src) {
        if (src.getColor() != ColorSpace.RGB) {
            Transform transform = ColorUtil.getTransform(src.getColor(), ColorSpace.RGB);
            if (transform == null) {
                throw new IllegalArgumentException("Unsupported input colorspace: " + src.getColor());
            }
            Picture out = Picture.create(src.getWidth(), src.getHeight(), ColorSpace.RGB);
            transform.transform(src, out);
            new RgbToBgr().transform(out, out);
            src = out;
        }
        BufferedImage dst = new BufferedImage(src.getCroppedWidth(), src.getCroppedHeight(),
                BufferedImage.TYPE_3BYTE_BGR);
        if (src.getCrop() == null)
            toBufferedImage(src, dst);
        else
            toBufferedImageCropped(src, dst);
        return dst;
    }
    private void toBufferedImage(Picture src, BufferedImage dst) {
        byte[] data = ((DataBufferByte) dst.getRaster().getDataBuffer()).getData();
        byte[] srcData = src.getPlaneData(0);
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) (srcData[i] + 128);
        }
    }
    private static void toBufferedImageCropped(Picture src, BufferedImage dst) {
        byte[] data = ((DataBufferByte) dst.getRaster().getDataBuffer()).getData();
        byte[] srcData = src.getPlaneData(0);
        int dstStride = dst.getWidth() * 3;
        int srcStride = src.getWidth() * 3;
        for (int line = 0, srcOff = 0, dstOff = 0; line < dst.getHeight(); line++) {
            for (int id = dstOff, is = srcOff; id < dstOff + dstStride; id += 3, is += 3) {
                data[id] = (byte) (srcData[is] + 128);
                data[id + 1] = (byte) (srcData[is + 1] + 128);
                data[id + 2] = (byte) (srcData[is + 2] + 128);
            }
            srcOff += srcStride;
            dstOff += dstStride;
        }
    }
}
