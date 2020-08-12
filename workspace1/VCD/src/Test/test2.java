package Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.sound.sampled.*;
import javax.sound.sampled.AudioFormat.Encoding;

import org.jaudiotagger.audio.flac.*;
import org.jaudiotagger.audio.flac.metadatablock.MetadataBlockDataPicture;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.flac.*;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;


public class test2 {
	 public static void main(String[] agrs) throws Exception
	 {
		 play("C:\\Users\\WuJinPeng\\Desktop\\作业\\java\\song\\Else\\G.E.M.邓紫棋 - 来自天堂的魔鬼.flac");
	 }
	 
	 
	 static void buff2Image(byte[] b,String tagSrc) throws Exception
	    {
	        FileOutputStream fout = new FileOutputStream(tagSrc);
	        //将字节写入文件
	        fout.write(b);
	        fout.close();
	    }
	 
	 static byte[] image2Bytes(String imgSrc) throws Exception
	    {
	        FileInputStream fin = new FileInputStream(new File(imgSrc));
	        //可能溢出,简单起见就不考虑太多,如果太大就要另外想办法，比如一次传入固定长度byte[]
	        byte[] bytes  = new byte[fin.available()];
	        //将文件内容写入字节数组，提供测试的case
	        fin.read(bytes);
	        fin.close();
	        return bytes;
	    }
	 
	 public static void play(String filePath) {
	        try {
	            // 文件流
	            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
	            // 文件编码
	            AudioFormat audioFormat = audioInputStream.getFormat();
	            System.out.println(audioFormat.getChannels());
	            AudioFormat a = new AudioFormat(Encoding.PCM_SIGNED, (float) 44100.0, 16, 2, 4, (float) 44100.0, false);
	            System.out.println(a);
	            //new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8) * channels, rate, bigEndian)
	            // 转换文件编码
	            if (audioFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
	                System.out.println(audioFormat.getEncoding());
	                audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, audioFormat.getSampleRate(), 16, audioFormat.getChannels(), audioFormat.getChannels() * 2, audioFormat.getSampleRate(), false);
	                // 将数据流也转换成指定编码
	                audioInputStream = AudioSystem.getAudioInputStream(audioFormat, audioInputStream);
	            }
	            //audioFormat = a;
	            System.out.println(audioFormat);	            // 打开输出设备
	            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat, AudioSystem.NOT_SPECIFIED);
	            // 使数据行得到一个播放设备
	            SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
	            // 将数据行用指定的编码打开
	            sourceDataLine.open(audioFormat);
	            // 使数据行得到数据时就开始播放
	            sourceDataLine.start();
	 
	            int bytesPerFrame = audioInputStream.getFormat().getFrameSize();
	            // 将流数据逐渐写入数据行,边写边播
	            int numBytes = 1024 * bytesPerFrame;
	            byte[] audioBytes = new byte[numBytes];
	            while (audioInputStream.read(audioBytes) != -1) {
	                sourceDataLine.write(audioBytes, 0, audioBytes.length);
	            }
	            sourceDataLine.drain();
	            sourceDataLine.stop();
	            sourceDataLine.close();
	 
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }


}
