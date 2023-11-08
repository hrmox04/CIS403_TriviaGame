package com.example.quizzicalpursuit;
import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.*;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    ArrayList<Category> category;
    ArrayList<Integer> faveList;
    Context context;
    int pos;
    SharedPreferences sp;


    public CategoryAdapter(Context context,ArrayList<Category> category) {
        this.category = category;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.category_layout,parent,false);
        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {
//        pos = position;
        Category c = category.get(position);
        holder.txtName.setText(""+c.name);


        try{
            String keeper = sp.getString("fa"," ");
            String holderArray[] = keeper.split(" ");
            for(int k = 0;k<holderArray.length;k++){
                faveList.add(Integer.parseInt(holderArray[k]));
            }

            if(faveList.contains(category.get(position).getId())){
                holder.btnFav.setImageResource(R.drawable.h1);
            }else{
                holder.btnFav.setImageResource(R.drawable.h2);
            }

            HashSet<Integer> set = new HashSet<>();
            List<Integer> result = new ArrayList<>();

            for (Integer item : faveList) {
                if (set.add(item)) {
                    result.add(item);
                }
            }

            faveList.clear();
            faveList.addAll(result);

        }catch(Exception e){
            //Log.d("HESH",e+"");
        }
    }

    @Override
    public int getItemCount() {
        return category.size();
    }


    class CategoryViewHolder extends RecyclerView.ViewHolder{
        TextView txtName;
        Button btnSelect;
        ImageButton btnFav;
        public CategoryViewHolder(@NonNull View view) {
            super(view);

            //intialize local buttons for recyclerview.
            txtName = view.findViewById(R.id.txtName);
            btnSelect = view.findViewById(R.id.btnSelect);
            btnFav = view.findViewById(R.id.btnFav);
            faveList = new ArrayList<>();


            sp = view.getContext().getSharedPreferences("FAVES", Context.MODE_PRIVATE);

            btnSelect.setBackgroundColor(Color.parseColor("#FF4CAF50"));

            //on btnselect being clicked perform the following.
            btnSelect.setOnClickListener(e->{
                Log.d("HESH","SPAM "+ txtName.getText());

                //set position value
                pos = getAdapterPosition();

                //create intent and generate bundle
                Intent i = new Intent(view.getContext(), GameActivity.class);
                Bundle b = new Bundle();

                //send CATegory and CATegoryID to gameactivity.
                b.putString("CAT",""+txtName.getText());
                b.putInt("CATID", category.get(pos).getId());

                //apply bundle and send values.
                i.putExtras(b);
                startActivity(view.getContext(),i,b);

                GameSounds.vineBoom(e.getContext());
            });

            btnFav.setOnClickListener(e->{
                if(!faveList.contains(category.get(getAdapterPosition()).getId())){
                    faveList.add(category.get(getAdapterPosition()).getId());
                    //btnFav.setImageResource(R.drawable.h1);
                }else{

                    faveList.remove(faveList.indexOf(category.get(getAdapterPosition()).getId()));
                    btnFav.setImageResource(R.drawable.h2);
                }

                if(faveList.contains(category.get(getAdapterPosition()).getId())){
                    btnFav.setImageResource(R.drawable.h1);
                }

                SharedPreferences.Editor ed = sp.edit();
                String fav = "";
                for(int j = 0;j<faveList.size();j++){
                    fav += faveList.get(j).toString() + " ";
                }

                Category c = category.get(getAdapterPosition());

                //for(int j = 0;j<category.size();j++){

                    if(faveList.contains(c.getId())){
                        Category ckeeper = c;
                        category.remove(c);

                        for(int k=0;k<category.size()-1;k++){
                            Category state = category.get(k+1);
                            category.set(k,state);
                        }
                        category.add(0,ckeeper);
                        notifyDataSetChanged();

                    }

                //}

                //category.sort(Comparator.comparing(faveList -> faveList.getId()));
                ed.putString("fa",fav);
                ed.apply();

                Log.d("HESH"," "+ faveList.toString());

            });

        }


    }
}
