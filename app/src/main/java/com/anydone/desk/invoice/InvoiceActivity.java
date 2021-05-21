package com.anydone.desk.invoice;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.anydone.desk.R;
import com.anydone.desk.adapters.InvoiceAdapter;
import com.anydone.desk.invoicedetail.InvoiceDetailActivity;
import com.anydone.desk.realm.model.Invoice;
import com.anydone.desk.utils.GlobalUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InvoiceActivity extends AppCompatActivity {
    private static final String TAG = "InvoiceActivity";
    @BindView(R.id.rv_invoice)
    RecyclerView rvInvoice;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    @BindView(R.id.iv_invoice_not_found)
    ImageView ivDataNotFound;

    private InvoiceAdapter invoiceAdapter;
    private BottomSheetDialog filterBottomSheet;
    private MaterialButton btnSearch;
    private EditText etFromDate, etTillDate;
    private TextView tvReset;
    private long from, to;
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        ButterKnife.bind(this);

        setToolbar();
        Invoice invoice = new Invoice();
        invoice.setAmount(4000);
        invoice.setCreatedAt(1607656745000L);
        invoice.setInvoiceId(23452);

        List<Invoice> invoiceList = new ArrayList<>();
        invoiceList.add(invoice);

        setUpRecyclerView(invoiceList);
        createFilterBottomSheet();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filter, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            toggleBottomSheet();
            return true;
        }

        return false;

    }

    private void setUpRecyclerView(List<Invoice> invoiceList) {
        rvInvoice.setLayoutManager(new LinearLayoutManager(this));
        if (!CollectionUtils.isEmpty(invoiceList)) {
            rvInvoice.setVisibility(View.VISIBLE);
            ivDataNotFound.setVisibility(View.GONE);
            invoiceAdapter = new InvoiceAdapter(invoiceList, this);
            invoiceAdapter.setOnItemClickListener(ticket -> {
                Intent i = new Intent(this, InvoiceDetailActivity.class);
                startActivity(i);
            });

            rvInvoice.setAdapter(invoiceAdapter);
        } else {
            rvInvoice.setVisibility(View.GONE);
            ivDataNotFound.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setToolbar() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources()
                .getDrawable(R.drawable.white_bg));

        SpannableStringBuilder str = new SpannableStringBuilder("Invoice");
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(str);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @SuppressLint("ClickableViewAccessibility")
    private void createFilterBottomSheet() {
        filterBottomSheet = new BottomSheetDialog(this,
                R.style.BottomSheetDialog);
        @SuppressLint("InflateParams") View view = getLayoutInflater()
                .inflate(R.layout.layout_bottomsheet_invoice, null);

        filterBottomSheet.setContentView(view);
        btnSearch = view.findViewById(R.id.btn_search);
        etFromDate = view.findViewById(R.id.et_from_date);
        etTillDate = view.findViewById(R.id.et_till_date);
        tvReset = view.findViewById(R.id.tv_reset);

        DatePickerDialog.OnDateSetListener fromDateListener = (view1, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            myCalendar.set(year, month, dayOfMonth, 0, 0, 0);
            updateFromDate();

            Calendar calendarFromDate = Calendar.getInstance();
            String[] fromDateSeparated = etFromDate.getText().toString().trim().split("/");

            calendarFromDate.set(Integer.parseInt(fromDateSeparated[0]),
                    Integer.parseInt(fromDateSeparated[1]) - 1,
                    Integer.parseInt(fromDateSeparated[2]));
            calendarFromDate.set(year, month, dayOfMonth, 0, 0, 0);
            from = calendarFromDate.getTimeInMillis();
            GlobalUtils.showLog(TAG, "manual from: " + from);
        };

        DatePickerDialog.OnDateSetListener tillDateListener = (view1, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            myCalendar.set(year, month, dayOfMonth, 23, 59, 59);
            updateToDate();

            Calendar calendarTillDate = Calendar.getInstance();
            String[] tillDateSeparated = etTillDate.getText().toString().trim().split("/");

            calendarTillDate.set(Integer.parseInt(tillDateSeparated[0]),
                    Integer.parseInt(tillDateSeparated[1]) - 1,
                    Integer.parseInt(tillDateSeparated[2]));
            calendarTillDate.set(year, month, dayOfMonth, 23, 59, 59);
            to = calendarTillDate.getTimeInMillis();
            GlobalUtils.showLog(TAG, "manual to: " + to);
        };

        etFromDate.setOnClickListener(v -> new DatePickerDialog(this,
                fromDateListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        etTillDate.setOnClickListener(v -> new DatePickerDialog(this,
                tillDateListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        tvReset.setOnClickListener(v -> {
            toggleBottomSheet();
            etFromDate.setText("");
            etTillDate.setText("");
        });

        btnSearch.setOnClickListener(v -> {
            String fromDate = etFromDate.getText().toString().trim();
            String tillDate = etTillDate.getText().toString().trim();

            if (!fromDate.isEmpty() && !tillDate.isEmpty()) {

                GlobalUtils.showLog(TAG, "final from: " + from);
                GlobalUtils.showLog(TAG, "final to: " + to);

                toggleBottomSheet();
            } else {
                Toast.makeText(this, "Please enter dates", Toast.LENGTH_SHORT).show();
            }

        });
    }


    private void updateFromDate() {
        String myFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etFromDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateToDate() {
        String myFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etTillDate.setText(sdf.format(myCalendar.getTime()));
    }


    public void toggleBottomSheet() {
        if (filterBottomSheet.isShowing()) filterBottomSheet.dismiss();
        else {
            filterBottomSheet.show();
        }
    }

}