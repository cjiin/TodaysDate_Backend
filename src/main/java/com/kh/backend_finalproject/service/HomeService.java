package com.kh.backend_finalproject.service;
import com.kh.backend_finalproject.constant.RegionStatus;
import com.kh.backend_finalproject.dto.PostBookmarkDto;
import com.kh.backend_finalproject.dto.PostUserDto;
import com.kh.backend_finalproject.entitiy.BookmarkTb;
import com.kh.backend_finalproject.entitiy.FolderTb;
import com.kh.backend_finalproject.entitiy.PostTb;
import com.kh.backend_finalproject.entitiy.UserTb;
import com.kh.backend_finalproject.repository.BookmarkRepository;
import com.kh.backend_finalproject.repository.FolderRepository;
import com.kh.backend_finalproject.repository.PostRepository;
import com.kh.backend_finalproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class HomeService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final BookmarkRepository bookmarkRepository;
    private final FolderRepository folderRepository;

    // ✅️전체 지역 게시글 작성일 최근순 정렬
    public List<PostUserDto> findAllPostsList() {
        List<PostUserDto> postUserDtos = postRepository.findAllPostsWithUserDetails();
        return postUserDtos;
    }
    // ✅️특정 지역 게시글 작성일 최근순 정렬
    public List<PostUserDto> findRegionPostsList(RegionStatus status) {
        List<PostUserDto> postUserDtos = postRepository.findRegionPostsWithUserDetails(status);
        return postUserDtos;
    }
    // ✅키워드 검색
    public List<PostTb> findByKeyword(String keyword) {
        List<PostTb> postList = postRepository.findByKeyword(keyword);
        return postList;
    }
    // ✅북마크 상위 5개 게시글 내림차순 정렬
    public Page<PostBookmarkDto> findTop5ByBookmarkCount() {
        Pageable topFive = PageRequest.of(0,5);
        Page<PostBookmarkDto> postBookmarkDtos = postRepository.findTop5ByBookmarkCount(topFive);
        return postBookmarkDtos;
    }
    // ✅회원 프로필 가져오기(by Email)
    public String findPfImgByEmail(String email) {
        UserTb user = userRepository.findByEmail(email);
        return user.getPfImg();
    }
    // ✅북마크 추가
    public boolean createBookmark(Long userId, Long postId, String folderName) {
        Optional<UserTb> userOptional = userRepository.findById(userId);
        Optional<PostTb> postOptional = postRepository.findById(postId);
        if(userOptional.isEmpty() || postOptional.isEmpty()) return false;

        UserTb user = userOptional.get();
        PostTb post = postOptional.get();

        FolderTb folder = folderRepository.findByNameAndUser(folderName, user)
                .orElseGet(() -> {
                    FolderTb newFolder = new FolderTb();
                    newFolder.setName(folderName);
                    newFolder.setUser(user);
                    return folderRepository.save(newFolder);
                });

        BookmarkTb bookmark = new BookmarkTb();
        bookmark.setFolder(folder);
        bookmark.setPost(post);
        bookmarkRepository.save(bookmark);

        return true;
    }
}