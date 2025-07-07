import axios from 'axios'
import Cookies from 'js-cookie'

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api'

// Create axios instance
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
})

// Request interceptor to add auth token
api.interceptors.request.use(
  (config) => {
    const token = Cookies.get('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// Response interceptor for error handling
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      Cookies.remove('token')
      Cookies.remove('user')
      window.location.href = '/auth/login'
    }
    return Promise.reject(error)
  }
)

// API Types
export interface ApiResponse<T = any> {
  success: boolean
  message: string
  data?: T
  error?: string
}

export interface User {
  id: number
  username: string
  email: string
  phoneNumber?: string
  roles: string[]
  createdAt: string
  updatedAt: string
}

export interface Room {
  id: number
  name: string
  type: string
  description: string
  pricePerNight: number
  maxOccupancy: number
  amenities: string[]
  images: string[]
  isAvailable: boolean
  createdAt: string
  updatedAt: string
}

export interface Booking {
  id: number
  room: Room
  guestName: string
  guestPhone: string
  guestEmail: string
  checkInDate: string
  checkOutDate: string
  totalAmount: number
  status: 'PENDING' | 'CONFIRMED' | 'CANCELLED' | 'COMPLETED'
  paymentStatus: 'PENDING' | 'PAID' | 'FAILED' | 'REFUNDED'
  paymentId?: string
  specialRequests?: string
  createdAt: string
  updatedAt: string
}

export interface MenuItem {
  id: number
  name: string
  category: string
  price: number
  description: string
  imageUrl?: string
  isAvailable: boolean
  createdAt: string
  updatedAt: string
}

export interface GalleryImage {
  id: number
  imageUrl: string
  category: string
  title: string
  description?: string
  uploadedAt: string
}

export interface BlogPost {
  id: number
  title: string
  content: string
  imageUrl?: string
  author: string
  excerpt?: string
  isPublished: boolean
  createdAt: string
  updatedAt: string
}

export interface Feedback {
  id: number
  guestName: string
  guestEmail?: string
  rating: number
  comment: string
  isApproved: boolean
  submittedAt: string
}

export interface ContactInfo {
  id: number
  phone: string
  email: string
  address: string
  mapLink?: string
  whatsappNumber?: string
  openingHours?: string
  updatedAt: string
}

// Auth API
export const authApi = {
  login: (email: string, password: string) =>
    api.post<ApiResponse<{ token: string; user: User }>>('/auth/login', { email, password }),
  
  register: (data: { username: string; email: string; password: string; phoneNumber?: string }) =>
    api.post<ApiResponse>('/auth/register', data),
  
  forgotPassword: (email: string) =>
    api.post<ApiResponse>('/auth/forgot-password', { email }),
  
  resetPassword: (token: string, newPassword: string) =>
    api.post<ApiResponse>('/auth/reset-password', { token, newPassword }),
  
  requestOtp: (phoneNumber: string) =>
    api.post<ApiResponse>('/auth/request-otp', { phoneNumber }),
  
  verifyOtp: (phoneNumber: string, otpCode: string, newPassword: string) =>
    api.post<ApiResponse>('/auth/verify-otp', { phoneNumber, otpCode, newPassword }),
}

// Rooms API
export const roomsApi = {
  getAll: () => api.get<ApiResponse<Room[]>>('/rooms'),
  getById: (id: number) => api.get<ApiResponse<Room>>(`/rooms/${id}`),
  getByType: (type: string) => api.get<ApiResponse<Room[]>>(`/rooms/type/${type}`),
  getAvailable: (checkIn: string, checkOut: string) =>
    api.get<ApiResponse<Room[]>>(`/rooms/available?checkIn=${checkIn}&checkOut=${checkOut}`),
}

// Bookings API
export const bookingsApi = {
  create: (data: {
    roomId: number
    guestName: string
    guestPhone: string
    guestEmail: string
    checkInDate: string
    checkOutDate: string
    specialRequests?: string
  }) => api.post<ApiResponse<Booking>>('/booking', data),
  
  getById: (id: number) => api.get<ApiResponse<Booking>>(`/booking/${id}`),
  getByEmail: (email: string) => api.get<ApiResponse<Booking[]>>(`/booking/guest/${email}`),
  checkAvailability: (roomId: number, checkIn: string, checkOut: string) =>
    api.get<ApiResponse<boolean>>(`/booking/check-availability?roomId=${roomId}&checkIn=${checkIn}&checkOut=${checkOut}`),
}

// Menu API
export const menuApi = {
  getAll: () => api.get<ApiResponse<MenuItem[]>>('/menu'),
  getById: (id: number) => api.get<ApiResponse<MenuItem>>(`/menu/${id}`),
  getByCategory: (category: string) => api.get<ApiResponse<MenuItem[]>>(`/menu/category/${category}`),
}

// Gallery API
export const galleryApi = {
  getAll: () => api.get<ApiResponse<GalleryImage[]>>('/gallery'),
  getById: (id: number) => api.get<ApiResponse<GalleryImage>>(`/gallery/${id}`),
  getByCategory: (category: string) => api.get<ApiResponse<GalleryImage[]>>(`/gallery/category/${category}`),
}

// Blog API
export const blogApi = {
  getAll: () => api.get<ApiResponse<BlogPost[]>>('/blog'),
  getById: (id: number) => api.get<ApiResponse<BlogPost>>(`/blog/${id}`),
  search: (title: string) => api.get<ApiResponse<BlogPost[]>>(`/blog/search?title=${title}`),
}

// Feedback API
export const feedbackApi = {
  getAll: () => api.get<ApiResponse<Feedback[]>>('/feedback'),
  create: (data: { guestName: string; guestEmail?: string; rating: number; comment: string }) =>
    api.post<ApiResponse<Feedback>>('/feedback', data),
  getAverageRating: () => api.get<ApiResponse<number>>('/feedback/average-rating'),
}

// Contact API
export const contactApi = {
  getInfo: () => api.get<ApiResponse<ContactInfo>>('/contact'),
  submitForm: (data: {
    name: string
    email: string
    phone: string
    subject?: string
    message: string
  }) => api.post<ApiResponse>('/contact', data),
}

// User API
export const userApi = {
  getProfile: () => api.get<ApiResponse<User>>('/user/profile'),
}

export default api