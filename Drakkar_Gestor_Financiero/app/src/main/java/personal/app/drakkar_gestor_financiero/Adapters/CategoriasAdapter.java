package personal.app.drakkar_gestor_financiero.Adapters;

import static personal.app.drakkar_gestor_financiero.PrincipalActivity.idcategoria;
import static personal.app.drakkar_gestor_financiero.PrincipalActivity.categoria;
import static personal.app.drakkar_gestor_financiero.PrincipalActivity.opccatad;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import personal.app.drakkar_gestor_financiero.CategoriasModEliActivty;
import personal.app.drakkar_gestor_financiero.Models.Categorias;
import personal.app.drakkar_gestor_financiero.R;

public class CategoriasAdapter extends RecyclerView.Adapter<CategoriasAdapter.ViewHolder> {

    //adaptador del RecyclerView de categorías.
    List<Categorias> lstCategorias;
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        TextView tvIdCL, tvCategoriaCL;
        ImageView ivCategoriaCL;
        ConstraintLayout clListaCategorias, pnlSeparador1CL;
        Context context;
        public  ViewHolder(View view,Context context){
            super(view);
            tvIdCL = view.findViewById(R.id.tvIdCL);
            tvCategoriaCL = view.findViewById(R.id.tvCategoriaCL);
            ivCategoriaCL = view.findViewById(R.id.ivCategoriaCL);
            clListaCategorias = view.findViewById(R.id.clListaCategorias);
            pnlSeparador1CL = view.findViewById(R.id.pnlSeparador1CL);
            this.context=context;
        }
    }

    public CategoriasAdapter(List<Categorias> lstCategorias, Context context) {
        this.context = context;
        this.lstCategorias = lstCategorias;
    }

    @NonNull
    @Override
    public CategoriasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.categorias_list,parent,false);
        ViewHolder vh=new ViewHolder(v,context);
        return vh;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Categorias obj = lstCategorias.get(position);
        if(lstCategorias.size()-1 == position){ //volver invisible el panel gris de abajo del último para que no aparezca.
            holder.pnlSeparador1CL.setVisibility(View.INVISIBLE);
        }
        holder.tvIdCL.setText(""+obj.getId());
        holder.tvCategoriaCL.setText(obj.getCategoria());
        if(obj.getTipo().equals("1")){ //si el tipo es 1 que setee la imagen down.
            holder.ivCategoriaCL.setImageResource(R.drawable.down);
        }else{ //si el tipo es 2 que setee la imagen up.
            holder.ivCategoriaCL.setImageResource(R.drawable.up);
        }
        holder.clListaCategorias.setOnClickListener(new View.OnClickListener() { //listener de cuando se le de click a los datos mostrados en el RecyclerView.
            @Override
            public void onClick(View view) {
                if(opccatad == 1){
                    //si es 1 entonces se esta accediendo desde la Activity CategoriasActivity que se
                    //accede desde el Fragment InicioFragment, por lo que al darle click a un elemento
                    //tiene que mandar a editarlo.
                    Intent intent = new Intent(context, CategoriasModEliActivty.class);
                    intent.putExtra("id",""+obj.getId());
                    intent.putExtra("categoria", obj.getCategoria());
                    view.getContext().startActivity(intent);
                }else{
                    //si es 2 entonces se esta accediendo desde el Fragment AgregarFragment es decir que
                    //estoy seleccionando una categoria, por lo que al darle click a un elemento
                    //tiene que almacenar el id y la categoria y asignarlos a las varibles idcategoria
                    //y categoria que se cargaron en el TextView del id y el EditTexts de la categoría
                    //del Fragment AgregarFragment.
                    idcategoria = ""+obj.getId();
                    categoria = obj.getCategoria();
                    ((Activity)view.getContext()).finish();
                }
            }
        });
    }

    @Override
    public int getItemCount() { return this.lstCategorias.size(); }

}