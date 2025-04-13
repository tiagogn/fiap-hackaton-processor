package br.com.fiap.hackaton.processor.infrastructure.processor.video;

import br.com.fiap.hackaton.processor.core.domain.StatusVideo;
import br.com.fiap.hackaton.processor.core.domain.Video;
import br.com.fiap.hackaton.processor.core.processor.video.SliceVideo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegLogCallback;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Component
@Slf4j
public class SliceVideoImpl implements SliceVideo {

    @Override
    @SneakyThrows
    public String execute(String key, Video video) {
        log.info("Processo do upload {} iniciado para o vídeo: {}", key, video.getName());

        // Configurar callback de log do FFmpeg para exibir mensagens detalhadas
        FFmpegLogCallback.set();

        String outputFolder = Files.createTempDirectory(video.getId().toString()) + File.separator;
        log.info("Pasta de saída: {}", outputFolder);

        log.info("Tamanho do arquivo: {} MB", video.getInputStream().available() / 1024 / 1024);

        try {
            // Abrir o arquivo de vídeo
            FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(video.getInputStream());

            // Configurar opções adicionais
            grabber.setOption("analyzeduration", "100000000");  // Aumentar tempo de análise
            grabber.setOption("probesize", "100000000");  // Aumentar tamanho de análise

            log.info("Iniciando o grabber...");
            grabber.start();

            // Obter a duração do vídeo em segundos
            double durationInSeconds = grabber.getLengthInTime() / 1000000.0;
            log.info("duração do vídeo: {} segundos, formato: {}, codec: {}, LxA: {} x {}, FPS: {}", durationInSeconds, grabber.getFormat(),
                    grabber.getVideoCodecName(), grabber.getImageWidth(), grabber.getImageHeight(), grabber.getFrameRate());

            // Definir o intervalo (20 segundos)
            int intervalInSeconds = 4;

            // Configurar conversor de frames para imagens
            Java2DFrameConverter converter = new Java2DFrameConverter();

            // Extrair frames em intervalos regulares
            for (int currentTimeInSeconds = 0; currentTimeInSeconds < durationInSeconds;
                 currentTimeInSeconds += intervalInSeconds) {
                log.info("Processando frame: {} segundos", currentTimeInSeconds);

                try {
                    // Posicionar o grabber no timestamp desejado
                    grabber.setTimestamp(currentTimeInSeconds * 1000000L);

                    // Capturar o frame atual
                    Frame frame = grabber.grabImage();
                    if (frame == null) {
                        log.warn("Não foi possível capturar o frame em {} segundos", currentTimeInSeconds);
                        continue;
                    }

                    // Converter o frame para imagem Java2D e salvar como JPG
                    String outputPath = outputFolder + "frame_at_" + currentTimeInSeconds + ".jpg";
                    saveFrame(converter, frame, outputPath);

                    log.info("Salvo: {}", outputPath);
                }
                catch (Exception ex) {
                    log.error("Erro ao processar frame em {} segundos: ", currentTimeInSeconds, ex);
                }
            }

            log.info("Finalizando o grabber...");
            grabber.stop();
            grabber.release();

            if (!hasImages(outputFolder)) {
                log.warn("Nenhuma imagem foi gerada no diretório {}", outputFolder);
                video.setStatus(StatusVideo.ERROR);
                return null;
            }

            video.setStatus(StatusVideo.PROCESSED);
        }
        catch (Exception ex) {
            log.error("Erro durante o processamento: {}", ex.getMessage(), ex);
            video.setStatus(StatusVideo.ERROR);
            return null;
        }

        return outputFolder;
    }

    private void saveFrame(Java2DFrameConverter converter, Frame frame, String outputPath)
            throws IOException {
        BufferedImage image = converter.convert(frame);

        if (image == null)
            throw new IOException("Não foi possível converter o frame para imagem");

        File outputFile = new File(outputPath);
        if (!ImageIO.write(image, "jpg", outputFile))
            throw new IOException("Não foi possível salvar a imagem no formato JPG");
    }

    private boolean hasImages(String outputFolder) {
        File folder = new File(outputFolder);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".jpg"));
        return files != null && files.length > 0;
    }
}