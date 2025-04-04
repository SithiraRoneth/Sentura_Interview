package lk.ijse.sentura_interview_usercrud.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ijse.sentura_interview_usercrud.dto.UserDTO;
import lk.ijse.sentura_interview_usercrud.service.WeavyApiClientUserService;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeavyApiClientUserServiceImpl implements WeavyApiClientUserService {

    @Value("${wys_iO6FOzlS1IIY63Bd2B5YaFaeaA8kyt0gz1GC}")
    private String apiKey;
    private final OkHttpClient client;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;

    @Override
    public boolean createUser(UserDTO userDTO) {
        try {
            String json = objectMapper.writeValueAsString(userDTO);

            RequestBody body = RequestBody.create(
                    json, okhttp3.MediaType.parse("application/json; charset=utf-8"));

            Request request = new Request.Builder()
                    .url("https://8015b5dbc0724d38882ac90397c27649.weavy.io")
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    System.out.println("User created successfully.");
                    return true;
                } else {
                    System.err.println("Failed to create user: " + response.code() + " - " + response.message());
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public UserDTO getUserDetails(String userId) throws Exception {
        Request request = new Request.Builder()
                .url("https://8015b5dbc0724d38882ac90397c27649.weavy.io/" + userId)
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String responseBody = response.body().string();

            return objectMapper.readValue(responseBody, UserDTO.class);

        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Error fetching user details: " + e.getMessage());
        }
    }

    @Override
    public List<UserDTO> listUser() throws Exception {
        Request request = new Request.Builder()
                .url("https://8015b5dbc0724d38882ac90397c27649.weavy.io")
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String responseBody = response.body().string();

            List<UserDTO> userDTOList = objectMapper.readValue(responseBody, objectMapper.getTypeFactory().constructCollectionType(List.class, UserDTO.class));

            if (userDTOList.isEmpty()) {
                return new ArrayList<>();
            }

            return modelMapper.map(userDTOList, new TypeToken<List<UserDTO>>() {}.getType());

        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Error");
        }
    }

    @Override
    public boolean updateUser(UserDTO userDTO, String userId) {
        try {
            String json = objectMapper.writeValueAsString(userDTO);

            RequestBody body = RequestBody.create(
                    json, okhttp3.MediaType.parse("application/json; charset=utf-8"));

            Request request = new Request.Builder()
                    .url("https://8015b5dbc0724d38882ac90397c27649.weavy.io/" + userId)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .put(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    System.out.println("User updated successfully.");
                    return true;
                } else {
                    System.err.println("Failed to update user: " + response.code() + " - " + response.message());
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteUser(String userId) {
        Request request = new Request.Builder()
                .url("https://b4a86c37cf10442eb928b8e9198520dd.weavy.io/api/users/" + userId + "/trash")
                .post(RequestBody.create(null, ""))
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
