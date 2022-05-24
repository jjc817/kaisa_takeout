package com.kaisa.dto;

import com.kaisa.entity.Setmeal;
import com.kaisa.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
