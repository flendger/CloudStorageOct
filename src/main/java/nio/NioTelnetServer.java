package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.util.stream.Stream;

import static nio.FileUtils.*;

public class NioTelnetServer {

    private final ByteBuffer buffer = ByteBuffer.allocate(1024);
    private final String rootPath = "server";
    private Path curPath;

    public NioTelnetServer() throws IOException {
        curPath = Path.of(rootPath);
        ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(8189));
        server.configureBlocking(false);
        Selector selector = Selector.open();
        server.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("Server started!");
        while (server.isOpen()) {
            selector.select();
            var selectionKeys = selector.selectedKeys();
            var iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                var key = iterator.next();
                if (key.isAcceptable()) {
                    handleAccept(key, selector);
                }
                if (key.isReadable()) {
                    handleRead(key, selector);
                }
                iterator.remove();
            }
        }
    }

    // TODO: 30.10.2020
    //  ls - список файлов (сделано на уроке),
    //  cd (name) - перейти в папку
    //  touch (name) создать текстовый файл с именем
    //  mkdir (name) создать директорию
    //  rm (name) удалить файл по имени
    //  copy (src, target) скопировать файл из одного пути в другой
    //  cat (name) - вывести в консоль содержимое файла

    private void handleRead(SelectionKey key, Selector selector) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        int read = channel.read(buffer);
        if (read == -1) {
            channel.close();
            return;
        }
        if (read == 0) {
            return;
        }
        buffer.flip();
        byte[] buf = new byte[read];
        int pos = 0;
        while (buffer.hasRemaining()) {
            buf[pos++] = buffer.get();
        }
        buffer.clear();
        String command = new String(buf, StandardCharsets.UTF_8)
                .replace("\n", "")
                .replace("\r", "");
        System.out.println(command);

        if (command.trim().isEmpty()) {
            return;
        }

        String[] commands = command.trim().split(" ");
        if (commands[0].equals("--help")) {
            sendMessageToChanel("input ls for show file list", channel);
        }
        if (commands[0].equals("ls")) {
            sendMessageToChanel(getFilesList(curPath.toString()), channel);
        }
        if (commands[0].equals("cd")) {
            try {
                String newPath = changeDir(commands[1], curPath.toString());
                curPath = Path.of(newPath);
                sendMessageToChanel(newPath, channel);
            } catch (NotDirectoryException e) {
                sendMessageToChanel(String.format("%s: is not directory", e.getFile()), channel);
            } catch (NoSuchFileException e) {
                sendMessageToChanel(String.format("%s: file or directory doesn't exist", e.getFile()), channel);
            }
        }
        if (commands[0].equals("mkdir")) {
            try {
                String newPath = makeDir(commands[1], curPath.toString());
                sendMessageToChanel(newPath, channel);
            } catch (FileAlreadyExistsException e) {
                sendMessageToChanel(String.format("%s: file or directory already exists", e.getFile()), channel);
            } catch (IOException e) {
                sendMessageToChanel(String.format("%s: unable to create new directory", commands[1]), channel);
            }
        }
        if (commands[0].equals("touch")) {
            try {
                String newPath = touchFile(commands[1], curPath.toString());
                sendMessageToChanel(newPath, channel);
            } catch (FileAlreadyExistsException e) {
                sendMessageToChanel(String.format("%s: file or directory already exists", e.getFile()), channel);
            } catch (IOException e) {
                sendMessageToChanel(String.format("%s: unable to create new file", commands[1]), channel);
            }
        }
        if (commands[0].equals("rm")) {
            try {
                String newPath = removeFile(commands[1], curPath.toString());
                sendMessageToChanel(newPath, channel);
            } catch (NoSuchFileException e) {
                sendMessageToChanel(String.format("%s: file or directory doesn't exist", e.getFile()), channel);
            } catch (IOException e) {
                sendMessageToChanel(String.format("%s: unable to remove file or directory", commands[1]), channel);
            }
        }
        if (commands[0].equals("copy")) {
            try {
                String newPath = copyFile(commands[1], commands[2]);
                sendMessageToChanel(newPath, channel);
            } catch (NoSuchFileException e) {
                sendMessageToChanel(String.format("%s: file or directory doesn't exist or src is directory", e.getFile()), channel);
            } catch (FileAlreadyExistsException e) {
                sendMessageToChanel(String.format("%s: file already exists", e.getFile()), channel);
            } catch (IOException e) {
                sendMessageToChanel(String.format("%s: unable to copy file", commands[1]), channel);
            }
        }
        if (commands[0].equals("cat")) {
            try {
                Stream<String> stream = catFile(Path.of(curPath.toString(), commands[1]).toString());
                sendMessageToChanel(String.format("Type file: %s", commands[1]), channel);
                sendStream(stream, channel);
            } catch (NoSuchFileException e) {
                sendMessageToChanel(String.format("%s: file doesn't exist or file is directory", e.getFile()), channel);
            } catch (IOException e) {
                sendMessageToChanel(String.format("%s: unable to type file", commands[1]), channel);
            }
        }
        if (commands[0].equals("close")) {
            channel.close();
        }
    }

    private void sendStream(Stream<String> stream, SocketChannel channel) throws IOException {
        Object[] lines = stream.toArray();
        for (Object line:lines
             ) {
            sendMessageToChanel(line.toString(), channel);
        }
    }

    private void sendMessageToChanel(String message, SocketChannel channel) throws IOException {
        channel.write(ByteBuffer.wrap((message + "\n\r").getBytes()));
    }

    private void sendMessage(String message, Selector selector) throws IOException {
        for (SelectionKey key : selector.keys()) {
            if (key.isValid() && key.channel() instanceof SocketChannel) {
                ((SocketChannel)key.channel())
                        .write(ByteBuffer.wrap(message.getBytes()));
            }
        }
    }

    private void handleAccept(SelectionKey key, Selector selector) throws IOException {
        SocketChannel channel = ((ServerSocketChannel) key.channel()).accept();
        channel.configureBlocking(false);
        System.out.println("Client accepted. IP: " + channel.getRemoteAddress());
        channel.register(selector, SelectionKey.OP_READ, "LOL");
        channel.write(ByteBuffer.wrap("Enter --help\n\r".getBytes()));
    }

    public static void main(String[] args) throws IOException {
        new NioTelnetServer();
    }
}
