package com.example.btlptudddn14.ui.statistics;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlptudddn14.common.ExpensesRecycleViewAdapter;
import com.example.btlptudddn14.databinding.FragmentStatisticsBinding;
import com.example.btlptudddn14.models.DBHandler;
import com.example.btlptudddn14.models.UserExpense;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class StatisticsFragment extends Fragment {

    private FragmentStatisticsBinding binding;
    private DBHandler dbHandler;
    private Button btnDaily, btnweekly, btnmonthly, btnyearly;

    private View screen1, screen2, screen3, screen4;
    private TextView displayDate, displayWeek, displayMonth, displayYear;

    PieChart dailyPieChart, weeklyPieChart, monthlyPiechart, yearlyPiechart;
    LineChart yearlyLineChart, monthlyLineChart;

    RecyclerView  dailyList, weeklyList, monthlyList, yearlyList;
    int[] colorIntArray = new int[]{
            Color.parseColor("#FF9800"),
            Color.parseColor("#F41414"),
            Color.parseColor("#2196F3"),
            Color.parseColor("#8BC34A"),
            Color.parseColor("#9C27B0"),
            Color.parseColor("#00BCD4"),
            Color.parseColor("#000000")
    };
    int[] colorClassArray = new int[]{Color.LTGRAY, Color.BLUE, Color.CYAN, Color.DKGRAY, Color.GREEN, Color.MAGENTA, Color.RED};
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentStatisticsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        screen1 = binding.dailyStatistic;
        screen2 = binding.weeklyStatistic;
        screen3 = binding.monthlyStatistic;
        screen4 = binding.yearlyStatistic;

        displayDate = binding.displayDate;
        displayWeek = binding.displayweek;
        displayMonth = binding.displayMonth;
        displayYear = binding.displayYear;




        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1); // Tháng bắt đầu từ 0
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String today = day + "-" + month + "-" + year;
        String currentMonth = month + "-" + year;


        displayDate.setText(" Current day: "+today);
        String currentWeek = getStartAndEndOfWeek(today).get(0)+" -- "+getStartAndEndOfWeek(today).get(1);
        displayWeek.setText(" "+currentWeek);
        displayMonth.setText(" Current month: "+currentMonth);
        displayYear.setText(" Current year: "+year);


        btnDaily = binding.btndaily;
        btnDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDatePickerDialog();

                showScreen1();


            }
        });


        btnweekly = binding.btnweekly;
        btnweekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWeekPickerDialog();
                showScreen2();
            }
        });


        btnmonthly = binding.btnmonthly;
        btnmonthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMonthPickerDialog();
                showScreen3();
            }
        });
        btnyearly = binding.btnyearly;
        btnyearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showYearPickerDialog();
                showScreen4();
            }
        });



        //Nhập dữ liệu cho biểu đồ thống kê ngày //
        dailyPieChart = binding.dailyPieChart; // lấy ra biểu đồ từ giao diện thông qua Id
        PieDataSet piedataset = new PieDataSet(dailydata1(today),"");

        // truyền vào dữ liệu từ phương thức đã xây dựng
        piedataset.setColors(getColorWithDate(today,today));
        // cài đặt màu sắc cho biểu đổ
        PieData pieData = new PieData(piedataset);
        dailyPieChart.setData(pieData);
        dailyPieChart.setCenterTextSize(22);
        dailyPieChart.setUsePercentValues(true);
        dailyPieChart.setCenterText("Total: "+getTotalAmount(today,today)+" USD");
        // cài đặt các yếu tố liên quan
        dailyPieChart.invalidate();

        ArrayList<UserExpense> list1 = new ArrayList<>();
        list1 = dbHandler.getInstance(getContext()).fetchExpensesBetweenDates(today,today);
        dailyList = binding.listDailyExpense;
        addPercent(list1,today,today);
        ExpensesRecycleViewAdapter adapter1 = new ExpensesRecycleViewAdapter(getContext(),list1);
        dailyList.setAdapter(adapter1);
        dailyList.setLayoutManager(new LinearLayoutManager(getContext()));



        //adapter1.notifyDataSetChanged();

        //////////////////////////////////////////

        //Nhập dữ liệu cho biểu đồ thống kê tuần //

        weeklyPieChart = binding.weeklyPieChart;
        PieDataSet piedataset2 = new PieDataSet(weeklydata1(getStartAndEndOfWeek(today).get(0),getStartAndEndOfWeek(today).get(1)),"");
        piedataset2.setColors(getColorWithDate(getStartAndEndOfWeek(today).get(0),getStartAndEndOfWeek(today).get(1)));
        PieData pieData2 = new PieData(piedataset2);
        weeklyPieChart.setData(pieData2);
        weeklyPieChart.setCenterTextSize(25);
        weeklyPieChart.setUsePercentValues(true);
        weeklyPieChart.setCenterText("Total: "+getTotalAmount(getStartAndEndOfWeek(today).get(0),getStartAndEndOfWeek(today).get(1))+" USD");
        weeklyPieChart.invalidate();



        ArrayList<UserExpense> list2 = new ArrayList<>();
        list2 = dbHandler.getInstance(getContext()).fetchExpensesBetweenDates(getStartAndEndOfWeek(today).get(0), getStartAndEndOfWeek(today).get(1));
        weeklyList = binding.listWeeklyExpense;
        addPercent(list2,getStartAndEndOfWeek(today).get(0), getStartAndEndOfWeek(today).get(1));
        ExpensesRecycleViewAdapter adapter2 = new ExpensesRecycleViewAdapter(getContext(),list2);
        weeklyList.setAdapter(adapter2);
        weeklyList.setLayoutManager(new LinearLayoutManager(getContext()));


        //////////////////////////////////////////

        //Nhập dữ liệu cho biểu đồ thống kê tháng //

        monthlyPiechart = binding.monthlyPieChart;
        PieDataSet piedataset3 = new PieDataSet(monthlydata1(getStartAndEndOfMonth(currentMonth).get(0),getStartAndEndOfMonth(currentMonth).get(1)),"");
        piedataset3.setColors(getColorWithDate(getStartAndEndOfMonth(currentMonth).get(0),getStartAndEndOfMonth(currentMonth).get(1)));
        PieData pieData3 = new PieData(piedataset3);
        monthlyPiechart.setData(pieData3);
        monthlyPiechart.setCenterTextSize(25);
        monthlyPiechart.setUsePercentValues(true);
        monthlyPiechart.setCenterText("Total: "+getTotalAmount(getStartAndEndOfMonth(currentMonth).get(0),getStartAndEndOfMonth(currentMonth).get(1))+" USD");
        monthlyPiechart.invalidate();


        monthlyLineChart = binding.monthlyLineChart;
        LineDataSet lineDataSet32 = new LineDataSet(monthlyLine1(getStartAndEndOfMonth(currentMonth).get(0),getStartAndEndOfMonth(currentMonth).get(1)),"monthly expense");
        lineDataSet32.setColor(Color.RED);

        ArrayList<ILineDataSet> dataSetsmonth = new ArrayList<>();
        dataSetsmonth.add(lineDataSet32);
        LineData lineData3 = new LineData(dataSetsmonth);

        final ArrayList<String> xLabelMonth = new ArrayList<>();
        for (int i = 1; i<= getDaysOfMonth(currentMonth); i++)
        {
            xLabelMonth.add(String.valueOf(i));
        }

        XAxis xAxis3 = monthlyLineChart.getXAxis();
        xAxis3.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis3.setAxisMinimum(0f);
        xAxis3.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return xLabelMonth.get((int) value % xLabelMonth.size());
            }
        });
        YAxis leftAxis3 = monthlyLineChart.getAxisLeft();
        leftAxis3.setAxisMinimum(0f);
        YAxis rightAxis3 = monthlyLineChart.getAxisRight();
        rightAxis3.setAxisMinimum(0f);

        monthlyLineChart.setDrawGridBackground(false);
        monthlyLineChart.setData(lineData3);
        monthlyLineChart.invalidate();

        ArrayList<UserExpense> list3 = new ArrayList<>();
        list3 = dbHandler.getInstance(getContext()).fetchExpensesBetweenDates(getStartAndEndOfMonth(currentMonth).get(0),getStartAndEndOfMonth(currentMonth).get(1));
        monthlyList = binding.listMonthlyExpense;
        addPercent(list3,getStartAndEndOfMonth(currentMonth).get(0),getStartAndEndOfMonth(currentMonth).get(1));
        ExpensesRecycleViewAdapter adapter3 = new ExpensesRecycleViewAdapter(getContext(),list3);
        monthlyList.setAdapter(adapter3);
        monthlyList.setLayoutManager(new LinearLayoutManager(getContext()));




        //////////////////////////////////////////


        //Nhập dữ liệu cho biểu đồ thống kê năm //
        yearlyPiechart = binding.yearlyPieChart;
        PieDataSet piedataset4 = new PieDataSet(yearlydata1("1-1-"+year,"31-12-"+year),"");
        piedataset4.setColors(getColorWithDate("1-1-"+year,"31-12-"+year));
        PieData pieData4 = new PieData(piedataset4);
        yearlyPiechart.setData(pieData4);
        yearlyPiechart.setCenterTextSize(25);
        yearlyPiechart.setUsePercentValues(true);
        yearlyPiechart.setCenterText("Total: "+getTotalAmount("1-1-"+year,"31-12-"+year)+" USD");
        yearlyPiechart.invalidate();




        yearlyLineChart = binding.yearlyLineChart;
        LineDataSet lineDataSet4 = new LineDataSet(yearlyLine1("1-1-"+year,"31-12-"+year),"yearly expense");
        lineDataSet4.setColor(Color.BLUE);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet4);
        LineData lineData = new LineData(dataSets);

        final ArrayList<String> xLabel = new ArrayList<>();
        xLabel.add("Jan");
        xLabel.add("Feb");
        xLabel.add("Mar");
        xLabel.add("Apr");
        xLabel.add("May");
        xLabel.add("Jun");
        xLabel.add("Jul");
        xLabel.add("Aug");
        xLabel.add("Sep");
        xLabel.add("Oct");
        xLabel.add("Nov");
        xLabel.add("Dec");

        XAxis xAxis = yearlyLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return xLabel.get((int) value % xLabel.size());
            }
        });
        YAxis leftAxis42 = yearlyLineChart.getAxisLeft();
        leftAxis42.setAxisMinimum(0f);
        YAxis rightAxis42 = yearlyLineChart.getAxisRight();
        rightAxis42.setAxisMinimum(0f);

        yearlyLineChart.setDrawGridBackground(false);
        yearlyLineChart.setData(lineData);
        yearlyLineChart.invalidate();

        ArrayList<UserExpense> list4 = new ArrayList<>();
        list4 = dbHandler.getInstance(getContext()).fetchExpensesBetweenDates("1-1-"+year,"31-12-"+year);
        yearlyList = binding.listYearlyExpense;
        addPercent(list4,"1-1-"+year,"31-12-"+year);
        ExpensesRecycleViewAdapter adapter4 = new ExpensesRecycleViewAdapter(getContext(),list4);
        yearlyList.setAdapter(adapter4);
        yearlyList.setLayoutManager(new LinearLayoutManager(getContext()));



        //////////////////////////////////////////

        return root;
    }


    private void dailyStatistic(String day){
        dailyPieChart = binding.dailyPieChart;

        PieDataSet piedataset = new PieDataSet(dailydata1(day),"");
        piedataset.setColors(getColorWithDate(day,day));
        PieData pieData = new PieData(piedataset);
        dailyPieChart.setData(pieData);
        dailyPieChart.setCenterTextSize(25);
        dailyPieChart.setUsePercentValues(true);
        dailyPieChart.setCenterText("Total: "+getTotalAmount(day,day)+" USD");
        dailyPieChart.invalidate();

        ArrayList<UserExpense> list1 = new ArrayList<>();
        list1 = dbHandler.getInstance(getContext()).fetchExpensesBetweenDates(day,day);
        dailyList = binding.listDailyExpense;
        addPercent(list1,day,day);
        ExpensesRecycleViewAdapter adapter1 = new ExpensesRecycleViewAdapter(getContext(),list1);
        dailyList.setAdapter(adapter1);
        dailyList.setLayoutManager(new LinearLayoutManager(getContext()));
    }
    private void weeklyStatistic(String startDay, String endDay){
        weeklyPieChart = binding.weeklyPieChart;
        PieDataSet piedataset2 = new PieDataSet(weeklydata1(startDay,endDay),"");
        piedataset2.setColors(getColorWithDate(startDay,endDay));
        PieData pieData2 = new PieData(piedataset2);
        weeklyPieChart.setData(pieData2);
        weeklyPieChart.setCenterTextSize(25);
        weeklyPieChart.setUsePercentValues(true);
        weeklyPieChart.setCenterText("Total: "+getTotalAmount(startDay,endDay)+" USD");
        weeklyPieChart.invalidate();


        ArrayList<UserExpense> list2 = new ArrayList<>();
        list2 = dbHandler.getInstance(getContext()).fetchExpensesBetweenDates(startDay, endDay);
        weeklyList = binding.listWeeklyExpense;
        addPercent(list2,startDay,endDay);
        ExpensesRecycleViewAdapter adapter2 = new ExpensesRecycleViewAdapter(getContext(),list2);
        weeklyList.setAdapter(adapter2);
        weeklyList.setLayoutManager(new LinearLayoutManager(getContext()));
    }
    private void monthlyStatistic(String startDay, String endDay){
        monthlyPiechart = binding.monthlyPieChart;
        ArrayList<PieEntry> a = new ArrayList<>();
        a= monthlydata1(startDay,endDay);
        PieDataSet piedataset3 = new PieDataSet(a,"");
        piedataset3.setColors(getColorWithDate(startDay,endDay));
        PieData pieData3 = new PieData(piedataset3);
        monthlyPiechart.setData(pieData3);
        monthlyPiechart.setCenterTextSize(25);
        monthlyPiechart.setUsePercentValues(true);
        monthlyPiechart.setCenterText("Total: "+getTotalAmount(startDay,endDay)+" USD");
        monthlyPiechart.invalidate();


        monthlyLineChart = binding.monthlyLineChart;
        ArrayList<Entry> b = new ArrayList<>();
        b = monthlyLine1(startDay,endDay);
        LineDataSet lineDataSet32 = new LineDataSet(b,"monthly expense");
        lineDataSet32.setColor(Color.RED);

        ArrayList<ILineDataSet> dataSetsmonth = new ArrayList<>();
        dataSetsmonth.add(lineDataSet32);
        LineData lineData3 = new LineData(dataSetsmonth);

        final ArrayList<String> xLabelMonth = new ArrayList<>();
        for (int i = 1; i<= getDaysOfMonth(getMonthYearOfDate(startDay)); i++)
        {
            xLabelMonth.add(String.valueOf(i));
        }

        XAxis xAxis3 = monthlyLineChart.getXAxis();
        xAxis3.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis3.setAxisMinimum(0f);
        xAxis3.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return xLabelMonth.get((int) value % xLabelMonth.size());
            }
        });
        YAxis leftAxis3 = monthlyLineChart.getAxisLeft();
        leftAxis3.setAxisMinimum(0f);
        YAxis rightAxis3 = monthlyLineChart.getAxisRight();
        rightAxis3.setAxisMinimum(0f);

        monthlyLineChart.setDrawGridBackground(false);
        monthlyLineChart.setData(lineData3);
        monthlyLineChart.invalidate();

        ArrayList<UserExpense> list3 = new ArrayList<>();
        list3 = dbHandler.getInstance(getContext()).fetchExpensesBetweenDates(startDay,endDay);
        monthlyList = binding.listMonthlyExpense;
        addPercent(list3,startDay,endDay);
        ExpensesRecycleViewAdapter adapter3 = new ExpensesRecycleViewAdapter(getContext(),list3);
        monthlyList.setAdapter(adapter3);
        monthlyList.setLayoutManager(new LinearLayoutManager(getContext()));
    }
    private void yearlyStatistic(String startDay, String endDay){
        yearlyPiechart = binding.yearlyPieChart;
        PieDataSet piedataset4 = new PieDataSet(yearlydata1(startDay, endDay),"");
        piedataset4.setColors(getColorWithDate(startDay,endDay));
        PieData pieData4 = new PieData(piedataset4);
        yearlyPiechart.setData(pieData4);
        yearlyPiechart.setCenterTextSize(25);
        yearlyPiechart.setUsePercentValues(true);
        yearlyPiechart.setCenterText("Total: "+getTotalAmount(startDay,endDay)+" USD");
        yearlyPiechart.invalidate();




        yearlyLineChart = binding.yearlyLineChart;
        LineDataSet lineDataSet4 = new LineDataSet(yearlyLine1(startDay, endDay),"yearly expense");
        lineDataSet4.setColor(Color.BLUE);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet4);
        LineData lineData = new LineData(dataSets);

        final ArrayList<String> xLabel = new ArrayList<>();
        xLabel.add("Jan");
        xLabel.add("Feb");
        xLabel.add("Mar");
        xLabel.add("Apr");
        xLabel.add("May");
        xLabel.add("Jun");
        xLabel.add("Jul");
        xLabel.add("Aug");
        xLabel.add("Sep");
        xLabel.add("Oct");
        xLabel.add("Nov");
        xLabel.add("Dec");

        XAxis xAxis = yearlyLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return xLabel.get((int) value % xLabel.size());
            }
        });
        YAxis leftAxis42 = yearlyLineChart.getAxisLeft();
        leftAxis42.setAxisMinimum(0f);
        YAxis rightAxis42 = yearlyLineChart.getAxisRight();
        rightAxis42.setAxisMinimum(0f);

        yearlyLineChart.setDrawGridBackground(false);
        yearlyLineChart.setData(lineData);
        yearlyLineChart.invalidate();

        ArrayList<UserExpense> list4 = new ArrayList<>();
        list4 = dbHandler.getInstance(getContext()).fetchExpensesBetweenDates(startDay, endDay);
        yearlyList = binding.listYearlyExpense;
        addPercent(list4,startDay,endDay);
        ExpensesRecycleViewAdapter adapter4 = new ExpensesRecycleViewAdapter(getContext(),list4);
        yearlyList.setAdapter(adapter4);
        yearlyList.setLayoutManager(new LinearLayoutManager(getContext()));

    }
    private int[] getColorWithDate(String startDay, String endDay){
        ArrayList<UserExpense> list = dbHandler.getInstance(getContext()).fetchExpensesBetweenDates(startDay,endDay);
        int color[] = new int[list.size()];
        for(int i = 0; i < list.size(); i++){
            color[i] = colorIntArray[list.get(i).getType()] ;
        }

        return color;
    }
    private ArrayList<UserExpense> addPercent(ArrayList<UserExpense> list, String startday, String endday){
        for(int i = 0; i < list.size();i++){
            double pc = list.get(i).getAmount()/getTotalAmount(startday,endday)*100;
            double percent = Math.round(pc*10);
            list.get(i).setDescription(String.valueOf(percent/10)+" %");
        }
        return list;
    }
    private String category(int type){
        String category = new String();
        if (type == 0) category = "FOOD_BEVERAGE";
        else if (type == 1) category = "HEALTH";
        else if (type == 2) category = "HOUSING";
        else if (type == 3) category = "INVESMENT";
        else if (type == 4) category = "TRANSPORT";
        else if (type == 5) category = "TRAVEL";
        else if (type == 6) category = "UNCATEGORIZED";
        else category = "";
        return category;
    }

    private ArrayList<PieEntry> dailydata1(String date){
        ArrayList<PieEntry> dataVals = new ArrayList<>();
        // Khai báo 1 ArrayList kiểu PieEntry
        ArrayList<UserExpense> userExpenses = new ArrayList<>();
        userExpenses = dbHandler.getInstance(getContext()).fetchExpensesBetweenDates(date, date);
        // Lấy dữ liệu từ database để truyền vào 1 ArrayList kiểu UserExpense
        int i = 0;
        while (i < userExpenses.size()){
            dataVals.add(new PieEntry((float)userExpenses.get(i).getAmount(),category(userExpenses.get(i).getType())));
            i++;
        }
        //sử dụng hàm add() để thêm dữ liệu vừa lấy được vào list của biểu đồ
        return dataVals;
    }
    private ArrayList<PieEntry> weeklydata1(String startDay, String endDay){
        ArrayList<PieEntry> dataVals = new ArrayList<>();
        ArrayList<UserExpense> userExpenses = new ArrayList<>();
        userExpenses = dbHandler.getInstance(getContext()).fetchExpensesBetweenDates(startDay, endDay);

        int i = 0;
        while (i < userExpenses.size()){
            dataVals.add(new PieEntry((float)userExpenses.get(i).getAmount(),category(userExpenses.get(i).getType())));
            i++;
        }
        return dataVals;
    }

    private ArrayList<PieEntry> monthlydata1(String startDay, String endDay){
        ArrayList<PieEntry> dataVals = new ArrayList<>();
        ArrayList<UserExpense> userExpenses = new ArrayList<>();
        userExpenses = dbHandler.getInstance(getContext()).fetchExpensesBetweenDates(startDay, endDay);

        int i = 0;
        while (i < userExpenses.size()){
            dataVals.add(new PieEntry((float)userExpenses.get(i).getAmount(),category(userExpenses.get(i).getType())));
            i++;
        }
        return dataVals;
    }
    private int getDaysOfMonth(String monthYear) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-yyyy");
            Date date = sdf.parse(monthYear);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        } catch (Exception e) {
            return -1; // Trả về giá trị âm để báo hiệu lỗi
        }
    }

    private String getMonthYearOfDate(String date) {

        while(true){
            char a = date.charAt(0);
            if(a != '-') date = date.substring(1);
            else {
                date = date.substring(1);
                break;
            }
        }
        return date;
    }
    private  double getTotalAmountOfDay(String date) {

        ArrayList<UserExpense> expenses = dbHandler.getInstance(getContext()).fetchExpensesByDate(date);
        double totalAmount = 0.0;
        for (UserExpense expense : expenses) {
            totalAmount += expense.getAmount();
        }
        return totalAmount;
    }
    private double getTotalAmount(String startDay, String endDay){
        ArrayList<UserExpense> expenses = dbHandler.getInstance(getContext()).fetchExpensesBetweenDates(startDay,endDay);
        double totalAmount = 0.0;
        for (UserExpense expense : expenses) {
            totalAmount += expense.getAmount();
        }
        return totalAmount;
    }


    private ArrayList<Entry> monthlyLine1(String startDay, String endDay){
        ArrayList<Entry> datavals = new ArrayList<>();

        String thisMonth = getMonthYearOfDate(startDay);
        for(int i = 1; i <= getDaysOfMonth(thisMonth);i++){
            String date = Integer.toString(i)+"-" + thisMonth;
            double totalAmount = getTotalAmountOfDay(date);
            datavals.add(new Entry(i,(float) totalAmount));
        }

        return datavals;
    }


    private ArrayList<PieEntry> yearlydata1(String startDay, String endDay){
        ArrayList<PieEntry> dataVals = new ArrayList<>();

        ArrayList<UserExpense> userExpenses = new ArrayList<>();
        userExpenses = dbHandler.getInstance(getContext()).fetchExpensesBetweenDates(startDay, endDay);

        int i = 0;
        while (i < userExpenses.size()){
            dataVals.add(new PieEntry((float)userExpenses.get(i).getAmount(),category(userExpenses.get(i).getType())));
            i++;
        }
        return dataVals;
    }
    private String getLastFourChars(String s) {
        if (s.length() > 4) {
            return s.substring(s.length() - 4);
        } else {
            return s;
        }
    }

    private  double getTotalAmountOfMonth(String month){
        double total = 0.0;
        for(int i = 1; i < getDaysOfMonth(month); i++)
        {
            String date = Integer.toString(i) + "-" + month;
            total += getTotalAmountOfDay(date);

        }
        return total;
    }
    private ArrayList<Entry> yearlyLine1(String startDay, String endDay){
        ArrayList<Entry> datavals = new ArrayList<>();
        String year = getLastFourChars(startDay);

        for(int i = 0; i < 12; i++)
        {   String month = Integer.toString(i+1) + "-"+year;
            datavals.add(new Entry((float)i,(float)getTotalAmountOfMonth(month)));
        }
        return datavals;
    }

    private void showScreen1() {
        screen1.setVisibility(View.VISIBLE);
        screen2.setVisibility(View.GONE);
        screen3.setVisibility(View.GONE);
        screen4.setVisibility(View.GONE);
    }

    private void showScreen2() {

        screen1.setVisibility(View.GONE);
        screen2.setVisibility(View.VISIBLE);
        screen3.setVisibility(View.GONE);
        screen4.setVisibility(View.GONE);
    }
    private void showScreen3() {

        screen1.setVisibility(View.GONE);
        screen2.setVisibility(View.GONE);
        screen3.setVisibility(View.VISIBLE);
        screen4.setVisibility(View.GONE);
    }
    private void showScreen4() {

        screen1.setVisibility(View.GONE);
        screen2.setVisibility(View.GONE);
        screen3.setVisibility(View.GONE);
        screen4.setVisibility(View.VISIBLE);
    }
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Xử lý sự kiện chọn ngày tháng năm ở đây
                        TextView displayDate = binding.displayDate;
                        String selectedDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        displayDate.setText(" Selected Date: " +selectedDate);
                        dailyStatistic(selectedDate);
                        Toast.makeText(getActivity(), "Selected Date: " + selectedDate, Toast.LENGTH_SHORT).show();
                    }
                },
                currentYear,
                currentMonth,
                currentDay);

        datePickerDialog.show();
    }
    private void showWeekPickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Xử lý sự kiện chọn ngày tháng năm ở đây

                        String selectedDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        ArrayList<String> week = getStartAndEndOfWeek(selectedDate);
                        displayWeek.setText(" "+week.get(0)+ " -- "+week.get(1));
                        weeklyStatistic(week.get(0),week.get(1));
                        Toast.makeText(getActivity(), "Selected Date: " + selectedDate, Toast.LENGTH_SHORT).show();
                    }
                },
                currentYear,
                currentMonth,
                currentDay);

        datePickerDialog.show();
    }
    private void showMonthPickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Xử lý sự kiện chọn ngày tháng năm ở đây

                        String selectedDate = (monthOfYear + 1) + "-" + year;
                        ArrayList<String> month = getStartAndEndOfMonth(selectedDate);
                        displayMonth.setText(" "+month.get(0)+ " -- "+month.get(1));
                        monthlyStatistic(month.get(0),month.get(1));
                        Toast.makeText(getActivity(), "Selected Month: " + selectedDate, Toast.LENGTH_SHORT).show();
                    }
                },
                currentYear,
                currentMonth,
                currentDay);

        datePickerDialog.show();
    }
    private void showYearPickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Xử lý sự kiện chọn ngày tháng năm ở đây
                        TextView displayDate = binding.displayYear;
                        String selectedDate = " Selected Year: " + year;
                        yearlyStatistic("1-1-"+year, "31-12-"+year);
                        displayDate.setText(selectedDate);
                        Toast.makeText(getActivity(), "Selected Year: " + selectedDate, Toast.LENGTH_SHORT).show();
                    }
                },
                currentYear,
                currentMonth,
                currentDay);

        datePickerDialog.show();
    }
    private ArrayList<String> getStartAndEndOfWeek(String date) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date chosenDate;
        try {
            chosenDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(chosenDate);

        // Đặt calendar về ngày đầu tuần (Thứ Hai)
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DATE, -1);
        }
        String startOfWeek = calendar.get(Calendar.DAY_OF_MONTH) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR);

        // Đặt calendar về ngày cuối tuần (Chủ Nhật)
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            calendar.add(Calendar.DATE, 1);
        }
        String endOfWeek = calendar.get(Calendar.DAY_OF_MONTH) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR);

        ArrayList<String> week = new ArrayList<>();
        week.add(startOfWeek);
        week.add(endOfWeek);
        return week;
    }
    private ArrayList<String> getStartAndEndOfMonth(String monthYear) {
        SimpleDateFormat format = new SimpleDateFormat("MM-yyyy");
        Date chosenMonth;
        try {
            chosenMonth = format.parse(monthYear);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(chosenMonth);

        // Đặt calendar về ngày đầu tháng
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        String startOfMonth = calendar.get(Calendar.DAY_OF_MONTH) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR);

        // Đặt calendar về ngày cuối tháng
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        String endOfMonth = calendar.get(Calendar.DAY_OF_MONTH) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR);

        ArrayList<String> month = new ArrayList<>();
        month.add(startOfMonth);
        month.add(endOfMonth);
        return month;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}