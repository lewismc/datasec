/**
 * 
 */
package org.graeme.datasec;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.FileDialog;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author lmcgibbn
 *
 */
public class DatasecApp {

  /**
   * Default constructor
   */
  public DatasecApp() {
  }
  protected Shell shell;
  private Text text;
  private Text text_1;
  private Text text_2;

  private static Cipher encrypt;
  private static Cipher decrypt;

  /**
   * encrypt and decrypt functions.
   */

  private void encrypt(InputStream input, OutputStream output)
      throws IOException {
    output = new CipherOutputStream(output, encrypt);
    writeBytes(input, output);
  }

  private void decrypt(InputStream input, OutputStream output)
      throws IOException {
    input = new CipherInputStream(input, decrypt);
    writeBytes(input, output);
  }

  private void writeBytes(InputStream input, OutputStream output)
      throws IOException {
    byte[] writeBuffer = new byte[512];
    int readBytes = 0;

    while ((readBytes = input.read(writeBuffer)) >= 0) {
      output.write(writeBuffer, 0, readBytes);
    }
    output.close();
    input.close();
  }

  /**
   * Open the application window enabling file encryption.
   */
  public void openFileEncryptionWIndow() {
    Display display = Display.getDefault();
    createContents();
    shell.open();
    shell.layout();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
  }

  /**
   * Create contents of the window.
   */
  protected void createContents() {
    // prepare the keys and iv
    byte[] keyBytes = new byte[] { 0x01, 0x23, 0x45, 0x67, (byte) 0x89,
        (byte) 0xab, (byte) 0xcd, (byte) 0xef };
    byte[] ivBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06,
        0x07 };
    final SecretKeySpec key = new SecretKeySpec(keyBytes, "DES");
    final IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

    shell = new Shell();
    shell.setSize(450, 300);
    shell.setText("Dataset Data Security Application");

    Button btnSelectAText = new Button(shell, SWT.NONE);
    btnSelectAText.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        try {
          final Shell shell = new Shell();
          FileDialog dlg = new FileDialog(shell, SWT.OPEN);
          String fileName = dlg.open();
          if (fileName != null) {
            text.setText(fileName);
          }
        } catch (Exception e1) {
          e1.printStackTrace();
        }
      }
    });
    btnSelectAText.setBounds(31, 69, 125, 25);
    btnSelectAText.setText("Select text file");

    Button btnEncrypt = new Button(shell, SWT.NONE);
    btnEncrypt.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        try {
          encrypt = Cipher.getInstance("DES/CBC/PKCS5Padding");
          encrypt.init(Cipher.ENCRYPT_MODE, key, ivSpec);
          encrypt(new FileInputStream(text.getText()),
              new FileOutputStream("Encrypted.txt"));
          text_1.setText("Encrypted.txt");
        } catch (Exception e1) {
          e1.printStackTrace();
        }
      }
    });
    btnEncrypt.setBounds(31, 119, 75, 25);
    btnEncrypt.setText("Encrypt");

    Button btnDecrypt = new Button(shell, SWT.NONE);
    btnDecrypt.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        try {
          decrypt = Cipher.getInstance("DES/CBC/PKCS5Padding");
          decrypt.init(Cipher.DECRYPT_MODE, key, ivSpec);
          decrypt(new FileInputStream(text_1.getText()),
              new FileOutputStream("Decrypted.txt"));
          text_2.setText("Decrypted.txt");
        } catch (Exception e1) {
          e1.printStackTrace();
        }
      }
    });
    btnDecrypt.setBounds(31, 165, 75, 25);
    btnDecrypt.setText("Decrypt");

    text = new Text(shell, SWT.BORDER);
    text.setBounds(155, 71, 249, 21);

    text_1 = new Text(shell, SWT.BORDER);
    text_1.setBounds(155, 121, 249, 21);

    text_2 = new Text(shell, SWT.BORDER);
    text_2.setBounds(155, 169, 249, 21);

  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    try {
      DatasecApp datasecApp = new DatasecApp();
      datasecApp.openFileEncryptionWIndow();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}