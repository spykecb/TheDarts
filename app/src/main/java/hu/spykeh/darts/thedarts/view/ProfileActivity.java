package hu.spykeh.darts.thedarts.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;


import hu.spykeh.darts.thedarts.R;
import hu.spykeh.darts.thedarts.model.Player;

public class ProfileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Player player =(Player) getIntent().getExtras().getSerializable("player");
        if(player != null) {
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try {
                BitMatrix bitMatrix = multiFormatWriter.encode(player.getQrcodeid(), BarcodeFormat.QR_CODE, 100, 100);
                BarcodeEncoder encoder = new BarcodeEncoder();
                Bitmap bitmap = encoder.createBitmap(bitMatrix);
                ImageView qrImage = (ImageView) this.findViewById(R.id.qrCodeView);
                TextView profileName = (TextView) this.findViewById(R.id.profile_playerName);
                profileName.setText(player.getName());
                qrImage.setImageBitmap(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }


    }

}
