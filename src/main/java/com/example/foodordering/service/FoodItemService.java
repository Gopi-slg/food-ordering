package com.example.foodordering.service;

import com.example.foodordering.dto.FoodItemRequest;
import com.example.foodordering.dto.FoodItemResponse;
import com.example.foodordering.entity.FoodItem;
import com.example.foodordering.exception.FoodNotFoundException;
import com.example.foodordering.repository.FoodItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodItemService {
    FoodItemRepository foodItemRepository;
    public FoodItemService(FoodItemRepository foodItemRepository){
        this.foodItemRepository=foodItemRepository;
    }

    public FoodItemResponse createFoodItem(FoodItemRequest request){
        FoodItem foodItem=new FoodItem();
        foodItem.setName(request.getName());
        foodItem.setDescription(request.getDescription());
        foodItem.setCategory(request.getCategory());
        foodItem.setPrice(request.getPrice());
        foodItem.setAvailable(request.getAvailable()==null || request.getAvailable());

        return toResponse(foodItemRepository.save(foodItem));
    }

    public List<FoodItemResponse> getAllFoodItems(){
        return foodItemRepository.findAll().stream().map(this::toResponse).toList();
    }

    public FoodItemResponse getFoodItemById(Long id){
        return toResponse(findFoodItem(id));
    }
    public FoodItem findFoodItem(Long id){
        return foodItemRepository.findById(id).
                orElseThrow(() -> new FoodNotFoundException("Food item not found with id: "+id));
    }
    public FoodItemResponse updateFoodItem(Long id, FoodItemRequest request){
        FoodItem existFoodItem=findFoodItem(id);

        existFoodItem.setName(request.getName());
        existFoodItem.setDescription(request.getDescription());
        existFoodItem.setCategory(request.getCategory());
        existFoodItem.setPrice(request.getPrice());

        if(request.getAvailable()!=null) existFoodItem.setAvailable(request.getAvailable());

        return toResponse(foodItemRepository.save(existFoodItem));
    }

    public void deleteFoodItem(Long id){
        foodItemRepository.delete(findFoodItem(id));
    }
    public FoodItemResponse toResponse(FoodItem foodItem){
        return new FoodItemResponse(foodItem.getId(),
                foodItem.getName(),
                foodItem.getDescription(),
                foodItem.getCategory(),
                foodItem.getPrice(),
                foodItem.getAvailable(),
                foodItem.getCreatedAt());
    }

}
