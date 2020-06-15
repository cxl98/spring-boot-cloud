package com.cxl.elFinder.command;

import com.cxl.elFinder.utils.ElFinderConstansts;
import com.cxl.elFinder.service.ElfinderStorage;
import com.cxl.elFinder.service.VolumeHandler;
import com.mortennobel.imagescaling.DimensionConstrain;
import com.mortennobel.imagescaling.ResampleOp;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class TmbCommand extends AbstractCommand implements ElFinderCommand {
    @Override
    public void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String target=request.getParameter(ElFinderConstansts.ELFINDER_PARAMETER_TARGET);

        VolumeHandler volumeHandler=findTarget(elfinderStorage,target);

        final DateTime dateTime=new DateTime();
        final String pattern="d MMM yyyy HH:mm:ss 'GMT'";
        final DateTimeFormatter dateTimeFormat=DateTimeFormat.forPattern(pattern);

        try(InputStream is=volumeHandler.openInputStream()){
            BufferedImage image= ImageIO.read(is);
            int width=80;
            ResampleOp resampleOp=new ResampleOp(DimensionConstrain.createMaxDimension(width,-1));
            resampleOp.setNumberOfThreads(4);
            BufferedImage b=resampleOp.filter(image,null);
            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
            ImageIO.write(b,"png",byteArrayOutputStream);


            response.setHeader("Last-Modified",dateTimeFormat.print(dateTime));
            response.setHeader("Expires",dateTimeFormat.print(dateTime.plusYears(1)));
            ImageIO.write(b,"png",response.getOutputStream());
        }

    }
}
