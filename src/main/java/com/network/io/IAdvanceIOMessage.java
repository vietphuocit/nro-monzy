package com.network.io;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface IAdvanceIOMessage extends IMessage {

    BufferedImage readImage() throws IOException;

    void writeImage(BufferedImage var1, String var2) throws IOException;

}