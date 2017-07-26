package com.example.acer.notex;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by acer on 18.07.2017.
 */

public class RecyclerClass extends RecyclerView.Adapter<RecyclerClass.ViewHolder> {
    ArrayList<Tasks> list1;
    Context c;
    RecyclerView recycler;

    public RecyclerClass(ArrayList<Tasks> list1, Context c,RecyclerView recycler) {
        this.list1 = list1;
        this.c = c;
        this.recycler=recycler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.recyclerlayout,parent,false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(c,ModifyNote.class);
                i.putExtra("NOTE",list1.get(recycler.getChildLayoutPosition(v)).getTaskName());
                Log.e("TAG",i.getStringExtra("NOTE"));
                i.putExtra("ID",list1.get(recycler.getChildLayoutPosition(v)).getId());
                c.startActivity(i);

            }
        });
        ViewHolder vh=new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Tasks t=list1.get(position);
        holder.recyclerText.setText(t.getTaskName());
        if(t.getTaskName().length()<=15)
        {
            holder.recyclerText.setTextSize(40);
            holder.recyclerCard.setCardBackgroundColor(Color.argb(255,110 ,245,218));

        }
        else{
            holder.recyclerText.setTextSize(20);
            holder.recyclerCard.setCardBackgroundColor(Color.argb(255,255,254,130));
        }


    }

    @Override
    public int getItemCount() {
        return list1.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
       TextView recyclerText;
        CardView recyclerCard;

        public ViewHolder(View itemView) {
            super(itemView);
            recyclerText=(TextView)itemView.findViewById(R.id.recyclertext);
            recyclerCard=(CardView)itemView.findViewById(R.id.recyclercard);
        }
    }
}
