package semen.sereda.yeticup.ui.team.find_team;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import semen.sereda.yeticup.R;

public class FindTeamFragment extends Fragment {
    private FindTeamViewModel findTeamViewModel;
    private String contents;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        findTeamViewModel = new ViewModelProvider(this).get(FindTeamViewModel.class);
        View root = inflater.inflate(R.layout.fragment_find_team, container, false);
        findTeamViewModel.getText().observe(getViewLifecycleOwner(), s -> {
        });
        Button scanner = root.findViewById(R.id.btn_scaner);
        Button apply = root.findViewById(R.id.btn_apply);

        scanner.setOnClickListener(v -> Scan());
        apply.setOnClickListener(v -> ApplyTeam());
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void Scan() {
        try {
            Intent i = new Intent("com.google.zxing.client.android.SCAN");
            i.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(i, 0);
        } catch (Exception e) {
            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
            startActivity(marketIntent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {

            if (resultCode == Activity.RESULT_OK) {
                contents = data.getStringExtra("SCAN_RESULT");
                /*TODO получение значения с qr-кода и проверка полученных данных:
                 *  вывод информации в TextView о команде: название команды и командира команды*/
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //handle cancel
            }
        }
    }

    private void ApplyTeam() {
        /*TODO проверка полей что они не пустые и отправка заявки на присоединение в команду
         * */
    }

    public void Create(View view) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode("editText.getText().toString()", BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            ((ImageView) view.findViewById(R.id.qr_code)).setImageBitmap(bmp);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}